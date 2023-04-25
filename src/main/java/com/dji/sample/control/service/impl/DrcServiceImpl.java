package com.dji.sample.control.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.config.MqttConfiguration;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.control.model.dto.*;
import com.dji.sample.control.model.enums.DrcMethodEnum;
import com.dji.sample.control.model.enums.DrcStatusErrorEnum;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.control.model.enums.MqttAclAccessEnum;
import com.dji.sample.control.model.param.DrcConnectParam;
import com.dji.sample.control.model.param.DrcModeParam;
import com.dji.sample.control.service.IControlService;
import com.dji.sample.control.service.IDrcService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.wayline.model.dto.WaylineTaskProgressReceiver;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.model.enums.WaylineTaskStatusEnum;
import com.dji.sample.wayline.model.param.UpdateJobParam;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.dji.sample.wayline.service.IWaylineRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
@Service
@Slf4j
public class DrcServiceImpl implements IDrcService {

    @Autowired
    private IMessageSenderService messageSenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private IDeviceService deviceService;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private ISendMessageService webSocketMessageService;

    @Autowired
    private IControlService controlService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IWaylineRedisService waylineRedisService;

    @Override
    public void setDrcModeInRedis(String dockSn, String clientId) {
        RedisOpsUtils.setWithExpire(RedisConst.DRC_PREFIX + dockSn, clientId, RedisConst.DRC_MODE_ALIVE_SECOND);
    }

    @Override
    public String getDrcModeInRedis(String dockSn) {
        return (String) RedisOpsUtils.get(RedisConst.DRC_PREFIX + dockSn);
    }

    @Override
    public Boolean delDrcModeInRedis(String dockSn) {
        return RedisOpsUtils.del(RedisConst.DRC_PREFIX + dockSn);
    }

    @Override
    public MqttBrokerDTO userDrcAuth(String workspaceId, String userId, String username, DrcConnectParam param) {

        // refresh token
        String clientId = param.getClientId();
        // first time
        if (!StringUtils.hasText(clientId) || !RedisOpsUtils.checkExist(RedisConst.MQTT_ACL_PREFIX + clientId)) {
            clientId = userId + "-" + System.currentTimeMillis();
            RedisOpsUtils.hashSet(RedisConst.MQTT_ACL_PREFIX + clientId, "", MqttAclAccessEnum.ALL.getValue());
        }

        String key = RedisConst.MQTT_ACL_PREFIX + clientId;

        try {
            RedisOpsUtils.expireKey(key, RedisConst.DRC_MODE_ALIVE_SECOND);

            return MqttConfiguration.getMqttBrokerWithDrc(
                    clientId, username, param.getExpireSec(), Collections.emptyMap());
        } catch (RuntimeException e) {
            RedisOpsUtils.del(key);
            throw e;
        }
    }

    private void checkDrcModeCondition(String workspaceId, String dockSn) {
        Optional<EventsReceiver<WaylineTaskProgressReceiver>> runningOpt = waylineRedisService.getRunningWaylineJob(dockSn);
        if (runningOpt.isPresent() && WaylineJobStatusEnum.IN_PROGRESS == waylineJobService.getWaylineState(dockSn)) {
            waylineJobService.updateJobStatus(workspaceId, runningOpt.get().getBid(),
                    UpdateJobParam.builder().status(WaylineTaskStatusEnum.PAUSE).build());
        }

        DockModeCodeEnum dockMode = deviceService.getDockMode(dockSn);
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isPresent() && (DockModeCodeEnum.IDLE == dockMode || DockModeCodeEnum.WORKING == dockMode)) {
            Optional<OsdSubDeviceReceiver> deviceOsd = deviceRedisService.getDeviceOsd(dockOpt.get().getChildDeviceSn(), OsdSubDeviceReceiver.class);
            if (deviceOsd.isEmpty() || deviceOsd.get().getElevation() <= 0) {
                throw new RuntimeException("The drone is not in the sky and cannot enter command flight mode.");
            }
        } else {
            throw new RuntimeException("The current state of the dock does not support entering command flight mode.");
        }

        ResponseResult result = controlService.seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (ResponseResult.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }

    }

    @Override
    public JwtAclDTO deviceDrcEnter(String workspaceId, DrcModeParam param) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + param.getDockSn() + TopicConst.DRC;
        String pubTopic = topic + TopicConst.DOWN;
        String subTopic = topic + TopicConst.UP;

        // If the dock is in drc mode, refresh the permissions directly.
        if (deviceService.checkDockDrcMode(param.getDockSn())
                && param.getClientId().equals(this.getDrcModeInRedis(param.getDockSn()))) {
            refreshAcl(param.getDockSn(), param.getClientId(), topic, subTopic);
            return JwtAclDTO.builder().sub(List.of(subTopic)).pub(List.of(pubTopic)).build();
        }

        checkDrcModeCondition(workspaceId, param.getDockSn());

        ServiceReply reply = messageSenderService.publishServicesTopic(
                param.getDockSn(), DrcMethodEnum.DRC_MODE_ENTER.getMethod(),
                DrcModeDTO.builder()
                    .mqttBroker(MqttConfiguration.getMqttBrokerWithDrc(param.getDockSn() + "-" + System.currentTimeMillis(), param.getDockSn(),
                            RedisConst.DRC_MODE_ALIVE_SECOND.longValue(),
                            Map.of(MapKeyConst.ACL, objectMapper.convertValue(JwtAclDTO.builder()
                                    .pub(List.of(subTopic))
                                    .sub(List.of(pubTopic))
                                    .build(), new TypeReference<Map<String, ?>>() {}))))
                    .build());

        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            throw new RuntimeException("SN: " + param.getDockSn() + "; Error Code:" + reply.getResult() + "; Failed to enter command flight control mode, please try again later!");
        }

        refreshAcl(param.getDockSn(), param.getClientId(), pubTopic, subTopic);
        return JwtAclDTO.builder().sub(List.of(subTopic)).pub(List.of(pubTopic)).build();
    }

    private void refreshAcl(String dockSn, String clientId, String pubTopic, String subTopic) {
        this.setDrcModeInRedis(dockSn, clientId);

        // assign acl，Match by clientId. https://www.emqx.io/docs/zh/v4.4/advanced/acl-redis.html
        // scheme: HSET mqtt_acl:[clientid] [topic] [access]
        String key = RedisConst.MQTT_ACL_PREFIX + clientId;
        RedisOpsUtils.hashSet(key, pubTopic, MqttAclAccessEnum.PUB.getValue());
        RedisOpsUtils.hashSet(key, subTopic, MqttAclAccessEnum.SUB.getValue());
    }

    @Override
    public void deviceDrcExit(String workspaceId, DrcModeParam param) {
        if (!deviceService.checkDockDrcMode(param.getDockSn())) {
            throw new RuntimeException("The dock is not in flight control mode.");
        }
        ServiceReply reply = messageSenderService.publishServicesTopic(
                param.getDockSn(), DrcMethodEnum.DRC_MODE_EXIT.getMethod(), "");
        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            throw new RuntimeException("SN: " + param.getDockSn() + "; Error Code:" +
                    reply.getResult() + "; Failed to exit command flight control mode, please try again later!");
        }

        String jobId = waylineRedisService.getPausedWaylineJobId(param.getDockSn());
        if (StringUtils.hasText(jobId)) {
            waylineJobService.updateJobStatus(workspaceId, jobId, UpdateJobParam.builder().status(WaylineTaskStatusEnum.RESUME).build());
        }

        this.delDrcModeInRedis(param.getDockSn());
        RedisOpsUtils.del(RedisConst.MQTT_ACL_PREFIX + param.getClientId());
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_DRC_STATUS_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleDrcStatusNotify(CommonTopicReceiver receiver, MessageHeaders headers) {
        String dockSn  = receiver.getGateway();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            return null;
        }

        DrcStatusNotifyReceiver eventsReceiver = mapper.convertValue(receiver.getData(), new TypeReference<DrcStatusNotifyReceiver>(){});
        if (DrcStatusErrorEnum.SUCCESS != eventsReceiver.getResult()) {
            webSocketMessageService.sendBatch(
                    deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(), BizCodeEnum.DRC_STATUS_NOTIFY.getCode(),
                    ResultNotifyDTO.builder().sn(dockSn)
                            .message(eventsReceiver.getResult().getErrorMsg())
                            .result(eventsReceiver.getResult().getErrorCode()).build());
        }
        return receiver;
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_DRC_MODE_EXIT_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleDrcModeExitNotify(CommonTopicReceiver receiver, MessageHeaders headers) {
        String dockSn  = receiver.getGateway();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            return null;
        }

        DrcModeReasonReceiver eventsReceiver = mapper.convertValue(receiver.getData(), new TypeReference<DrcModeReasonReceiver>(){});
        webSocketMessageService.sendBatch(
                deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(), BizCodeEnum.JOYSTICK_INVALID_NOTIFY.getCode(),
                        ResultNotifyDTO.builder().sn(dockSn)
                                .message(eventsReceiver.getReason().getMessage())
                                .result(eventsReceiver.getReason().getVal()).build());
        return receiver;
    }

}
