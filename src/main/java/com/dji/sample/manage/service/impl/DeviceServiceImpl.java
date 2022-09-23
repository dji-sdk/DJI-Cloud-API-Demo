package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttTopicService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.manage.dao.IDeviceMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceEntity;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.enums.DeviceFirmwareStatusEnum;
import com.dji.sample.manage.model.enums.IconUrlEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.*;
import com.dji.sample.manage.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
@Service
@Slf4j
@Transactional
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private IDeviceMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private IMqttTopicService topicService;

    @Autowired
    private IWorkspaceService workspaceService;

    @Autowired
    private IDevicePayloadService payloadService;

    @Autowired
    private ISendMessageService sendMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisOpsUtils redisOps;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private IDeviceFirmwareService deviceFirmwareService;

    @Autowired
    @Qualifier("gatewayOSDServiceImpl")
    private ITSAService tsaService;

    @Override
    public Boolean deviceOffline(String gatewaySn) {
        this.subscribeTopicOnline(gatewaySn);

        // Only the remote controller is logged in and the aircraft is not connected.
        String key = RedisConst.DEVICE_ONLINE_PREFIX + gatewaySn;

        boolean exist = redisOps.checkExist(key);
        if (!exist) {
            Optional<DeviceDTO> gatewayOpt = this.getDeviceBySn(gatewaySn);
            if (gatewayOpt.isPresent()) {
                DeviceDTO value = gatewayOpt.get();
                value.setChildDeviceSn(value.getDeviceSn());
                value.setBoundTime(null);
                value.setLoginTime(null);
                redisOps.setWithExpire(key, value, RedisConst.DEVICE_ALIVE_SECOND);
                this.pushDeviceOnlineTopo(value.getWorkspaceId(), gatewaySn, gatewaySn);
                return true;
            }
            DeviceDTO gateway = DeviceDTO.builder()
                    .deviceSn(gatewaySn)
                    .childDeviceSn(gatewaySn)
                    .domain(DeviceDomainEnum.GATEWAY.getDesc())
                    .build();
            gatewayOpt.map(DeviceDTO::getWorkspaceId).ifPresent(gateway::setWorkspaceId);
            redisOps.setWithExpire(key, gateway, RedisConst.DEVICE_ALIVE_SECOND);
            this.pushDeviceOnlineTopo(gateway.getWorkspaceId(), gatewaySn, gatewaySn);
            return true;
        }

        String deviceSn = ((DeviceDTO)(redisOps.get(key))).getChildDeviceSn();
        if (deviceSn.equals(gatewaySn)) {
            return true;
        }

        return subDeviceOffline(deviceSn);
    }

    @Override
    public Boolean subDeviceOffline(String deviceSn) {
        // Cancel drone-related subscriptions.
        this.unsubscribeTopicOffline(deviceSn);

        payloadService.deletePayloadsByDeviceSn(new ArrayList<>(List.of(deviceSn)));
        // If no information about this gateway device exists in the database, the drone is considered to be offline.
        String key = RedisConst.DEVICE_ONLINE_PREFIX + deviceSn;
        if (!redisOps.checkExist(key) || redisOps.getExpire(key) <= 0) {
            log.debug("The drone is already offline.");
            return true;
        }
        DeviceDTO device = (DeviceDTO) redisOps.get(key);
        // Publish the latest device topology information in the current workspace.
        this.pushDeviceOfflineTopo(device.getWorkspaceId(), deviceSn);

        redisOps.del(key);
        log.debug("{} offline.", deviceSn);
        return true;
    }

    @Override
    public Boolean deviceOnline(StatusGatewayReceiver deviceGateway) {
        String deviceSn = deviceGateway.getSubDevices().get(0).getSn();
        String key = RedisConst.DEVICE_ONLINE_PREFIX + deviceSn;
        // change log:  Use redis instead of
        long time = redisOps.getExpire(key);
        long gatewayTime = redisOps.getExpire(RedisConst.DEVICE_ONLINE_PREFIX + deviceGateway.getSn());
        long now = System.currentTimeMillis();

        if (time > 0 && gatewayTime > 0) {
            redisOps.expireKey(key, RedisConst.DEVICE_ALIVE_SECOND);
            DeviceDTO device = DeviceDTO.builder().loginTime(LocalDateTime.now()).deviceSn(deviceSn).build();
            DeviceDTO gateway = DeviceDTO.builder()
                    .loginTime(LocalDateTime.now())
                    .deviceSn(deviceGateway.getSn())
                    .childDeviceSn(deviceSn).build();
            this.updateDevice(gateway);
            this.updateDevice(device);
            String workspaceId = ((DeviceDTO)(redisOps.get(key))).getWorkspaceId();
            if (StringUtils.hasText(workspaceId)) {
                this.subscribeTopicOnline(deviceSn);
                this.subscribeTopicOnline(deviceGateway.getSn());
            }
            log.warn("{} is already online.", deviceSn);
            return true;
        }

        List<DeviceDTO> gatewaysList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .childSn(deviceSn)
                        .build());
        gatewaysList.stream().filter(
                gateway -> !gateway.getDeviceSn().equals(deviceGateway.getSn()))
                .findAny()
                .ifPresent(gateway -> {
                    gateway.setChildDeviceSn("");
                    this.updateDevice(gateway);
                });


        DeviceEntity gateway = deviceGatewayConvertToDeviceEntity(deviceGateway);
        gateway.setChildSn(deviceSn);
        // Set the icon of the gateway device displayed in the pilot's map, required in the TSA module.
        gateway.setUrlNormal(IconUrlEnum.NORMAL_PERSON.getUrl());
        // Set the icon of the gateway device displayed in the pilot's map when it is selected, required in the TSA module.
        gateway.setUrlSelect(IconUrlEnum.SELECT_PERSON.getUrl());
        gateway.setLoginTime(now);

        DeviceEntity subDevice = subDeviceConvertToDeviceEntity(deviceGateway.getSubDevices().get(0));
        // Set the icon of the drone device displayed in the pilot's map when it is selected, required in the TSA module.
        subDevice.setUrlNormal(IconUrlEnum.NORMAL_EQUIPMENT.getUrl());
        // Set the icon of the drone device displayed in the pilot's map, required in the TSA module.
        subDevice.setUrlSelect(IconUrlEnum.SELECT_EQUIPMENT.getUrl());
        subDevice.setLoginTime(now);

        // dock go online
        if (deviceGateway.getDomain() != null && DeviceDomainEnum.DOCK.getVal() == deviceGateway.getDomain()) {
            Optional<DeviceDTO> deviceOpt = this.getDeviceBySn(deviceGateway.getSn());
            if (deviceOpt.isEmpty()) {
                log.info("The dock is not bound and cannot go online.");
                return false;
            }
            gateway.setNickname(null);
            subDevice.setNickname(null);
        }

        Optional<DeviceEntity> gatewayOpt = this.saveDevice(gateway);
        String workspaceId = this.saveDevice(subDevice).orElse(subDevice).getWorkspaceId();

        redisOps.setWithExpire(RedisConst.DEVICE_ONLINE_PREFIX + deviceSn,
                DeviceDTO.builder()
                        .deviceSn(deviceSn)
                        .domain(DeviceDomainEnum.SUB_DEVICE.getDesc())
                        .workspaceId(workspaceId)
                        .build(),
                RedisConst.DEVICE_ALIVE_SECOND);
        redisOps.setWithExpire(RedisConst.DEVICE_ONLINE_PREFIX + gateway.getDeviceSn(),
                DeviceDTO.builder()
                        .deviceSn(gateway.getDeviceSn())
                        .workspaceId(gatewayOpt.orElse(gateway).getWorkspaceId())
                        .childDeviceSn(deviceSn)
                        .domain(deviceGateway.getDomain() != null ?
                                DeviceDomainEnum.getDesc(deviceGateway.getDomain()) :
                                DeviceDomainEnum.GATEWAY.getDesc())
                        .build(),
                RedisConst.DEVICE_ALIVE_SECOND);
        log.debug("{} online.", subDevice.getDeviceSn());

        if (StringUtils.hasText(workspaceId)) {
            this.pushDeviceOnlineTopo(workspaceId, deviceGateway.getSn(), deviceSn);
        }
        // Subscribe to topic related to drone devices.
        this.subscribeTopicOnline(deviceSn);
        this.subscribeTopicOnline(deviceGateway.getSn());
        return true;
    }

    @Override
    public void subscribeTopicOnline(String sn) {
        String[] subscribedTopic = topicService.getSubscribedTopic();
        for (String s : subscribedTopic) {
            // If you have already subscribed to the topic of the device, you do not need to subscribe again.
            if (s.contains(sn)) {
                return;
            }
        }
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + OSD_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + STATE_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + SERVICES_SUF + _REPLY_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + REQUESTS_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + EVENTS_SUF);
    }

    @Override
    public void unsubscribeTopicOffline(String sn) {
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + OSD_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + STATE_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + SERVICES_SUF + _REPLY_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + REQUESTS_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + EVENTS_SUF);
    }

    @Override
    public Boolean delDeviceByDeviceSns(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        return mapper.delete(new LambdaQueryWrapper<DeviceEntity>()
                .in(DeviceEntity::getDeviceSn, ids))
                > 0;
    }

    @Override
    public void publishStatusReply(String sn, CommonTopicResponse<Object> response) {
        Map<String, Integer> result = new ConcurrentHashMap<>(1);
        result.put("result", 0);
        response.setData(result);

        messageSender.publish(
                new StringBuilder()
                        .append(BASIC_PRE)
                        .append(PRODUCT)
                        .append(sn)
                        .append(STATUS_SUF)
                        .append(_REPLY_SUF)
                        .toString(),
                response);
    }

    @Override
    public List<DeviceDTO> getDevicesByParams(DeviceQueryParam param) {
        return mapper.selectList(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(StringUtils.hasText(param.getDeviceSn()),
                                DeviceEntity::getDeviceSn, param.getDeviceSn())
                        .eq(param.getDeviceType() != null,
                                DeviceEntity::getDeviceType, param.getDeviceType())
                        .eq(param.getSubType() != null,
                                DeviceEntity::getSubType, param.getSubType())
                        .eq(StringUtils.hasText(param.getChildSn()),
                                DeviceEntity::getChildSn, param.getChildSn())
                        .and(!CollectionUtils.isEmpty(param.getDomains()), wrapper -> {
                            for (Integer domain : param.getDomains()) {
                                wrapper.eq(DeviceEntity::getDomain, domain).or();
                            }
                        })
                        .eq(StringUtils.hasText(param.getWorkspaceId()),
                                DeviceEntity::getWorkspaceId, param.getWorkspaceId())
                        .eq(param.getBoundStatus() != null, DeviceEntity::getBoundStatus, param.getBoundStatus())
                        .orderBy(param.isOrderBy(),
                                param.isAsc(), DeviceEntity::getId))
                .stream()
                .map(this::deviceEntityConvertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDTO> getDevicesTopoForWeb(String workspaceId) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.SUB_DEVICE.getVal()))
                        .build());

        devicesList.forEach(device -> {
            this.spliceDeviceTopo(device);
            device.setWorkspaceId(workspaceId);
            device.setStatus(redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + device.getDeviceSn()));
        });
        return devicesList;
    }

    @Override
    public void spliceDeviceTopo(DeviceDTO device) {

        // remote controller
        List<DeviceDTO> gatewaysList = getDevicesByParams(
                DeviceQueryParam.builder()
                        .childSn(device.getDeviceSn())
                        .build());

        // payloads
        List<DevicePayloadDTO> payloadsList = payloadService
                .getDevicePayloadEntitiesByDeviceSn(device.getDeviceSn());


        device.setGatewaysList(gatewaysList);
        device.setPayloadsList(payloadsList);

    }

    @Override
    public Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn) {
        if (sn.isBlank()) {
            return Optional.empty();
        }
        List<TopologyDeviceDTO> topologyDeviceList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(sn)
                        .build())
                .stream()
                .map(this::deviceConvertToTopologyDTO)
                .collect(Collectors.toList());
        if (topologyDeviceList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(topologyDeviceList.get(0));
    }

    @Override
    public void pushDeviceOnlineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn, String gatewaySn) {

        CustomWebSocketMessage<TopologyDeviceDTO> pilotMessage =
                CustomWebSocketMessage.<TopologyDeviceDTO>builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_ONLINE.getCode())
                        .data(new TopologyDeviceDTO())
                        .build();

        this.getDeviceTopoForPilot(sn)
                .ifPresent(pilotMessage::setData);
        boolean exist = redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + sn);
        pilotMessage.getData().setOnlineStatus(exist);
        pilotMessage.getData().setGatewaySn(gatewaySn.equals(sn) ? "" : gatewaySn);

        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    @Override
    public TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device) {
        TopologyDeviceDTO.TopologyDeviceDTOBuilder builder = TopologyDeviceDTO.builder();

        if (device != null) {
            int domain = DeviceDomainEnum.getVal(device.getDomain());
            String subType = String.valueOf(device.getSubType());
            String type = String.valueOf(device.getType());

            builder.sn(device.getDeviceSn())
                    .deviceCallsign(device.getNickname())
                    .deviceModel(DeviceModelDTO.builder()
                            .domain(String.valueOf(domain))
                            .subType(subType)
                            .type(type)
                            .key(domain + "-" + type + "-" + subType)
                            .build())
                    .iconUrls(device.getIconUrl())
                    .onlineStatus(redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + device.getDeviceSn()))
                    .boundStatus(device.getBoundStatus())
                    .model(device.getDeviceName())
                    .userId(device.getUserId())
                    .domain(DeviceDomainEnum.getDesc(domain))
                    .build();
        }
        return builder.build();
    }

    @Override
    public void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn) {

        // All connected accounts in this workspace.
        Collection<ConcurrentWebSocketSession> allSessions = webSocketManageService.getValueWithWorkspace(workspaceId);

        if (!gatewaySn.equals(deviceSn)) {
            this.pushDeviceOnlineTopo(allSessions, deviceSn, gatewaySn);
            this.pushDeviceUpdateTopo(allSessions, deviceSn);
        }
        this.pushDeviceOnlineTopo(allSessions, gatewaySn, gatewaySn);
        this.pushDeviceUpdateTopo(allSessions, gatewaySn);
    }

    @Override
    public void pushDeviceOfflineTopo(String workspaceId, String sn) {
        // All connected accounts of this workspace.
        Collection<ConcurrentWebSocketSession> allSessions = webSocketManageService
                .getValueWithWorkspace(workspaceId);

        this.pushDeviceOfflineTopo(allSessions, sn);
        this.pushDeviceUpdateTopo(allSessions, sn);
    }

    @Override
    public void handleOSD(String topic, byte[] payload) {
        CommonTopicReceiver receiver;
        try {
            String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(),
                    topic.indexOf(OSD_SUF));

            // Real-time update of device status in memory
            redisOps.expireKey(RedisConst.DEVICE_ONLINE_PREFIX + from, RedisConst.DEVICE_ALIVE_SECOND);

            DeviceDTO device = (DeviceDTO) redisOps.get(RedisConst.DEVICE_ONLINE_PREFIX + from);

            if (device == null) {
                Optional<DeviceDTO> deviceOpt = this.getDeviceBySn(from);
                if (deviceOpt.isEmpty()) {
                    return;
                }
                device = deviceOpt.get();
                if (!StringUtils.hasText(device.getWorkspaceId())) {
                    return;
                }
                redisOps.setWithExpire(RedisConst.DEVICE_ONLINE_PREFIX + from, device,
                        RedisConst.DEVICE_ALIVE_SECOND);
                this.subscribeTopicOnline(from);
            }

            receiver = objectMapper.readValue(payload, CommonTopicReceiver.class);

            CustomWebSocketMessage<TelemetryDTO> wsMessage = CustomWebSocketMessage.<TelemetryDTO>builder()
                    .timestamp(System.currentTimeMillis())
                    .data(TelemetryDTO.builder().sn(from).build())
                    .build();

            Collection<ConcurrentWebSocketSession> webSessions = webSocketManageService
                    .getValueWithWorkspaceAndUserType(
                            device.getWorkspaceId(), UserTypeEnum.WEB.getVal());


            tsaService.handleOSD(receiver, device, webSessions, wsMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notify the pilot side that there is an update of the device topology.
     * @param sessions
     * @param deviceSn
     */
    private void pushDeviceUpdateTopo(Collection<ConcurrentWebSocketSession> sessions, String deviceSn) {

        CustomWebSocketMessage pilotMessage =
                CustomWebSocketMessage.builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_UPDATE_TOPO.getCode())
                        .data(new TopologyDeviceDTO())
                        .build();
        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    /**
     * Notify the pilot side that device is offline and needs to reacquire topology information.
     * @param sessions
     * @param sn
     */
    private void pushDeviceOfflineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn) {
        CustomWebSocketMessage<TopologyDeviceDTO> pilotMessage =
                CustomWebSocketMessage.<TopologyDeviceDTO>builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_OFFLINE.getCode())
                        .data(TopologyDeviceDTO.builder()
                                .sn(sn)
                                .onlineStatus(false)
                                .build())
                        .build();
        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    /**
     * Save the device information and update the information directly if the device already exists.
     * @param entity
     * @return
     */
    private Optional<DeviceEntity> saveDevice(DeviceEntity entity) {
        DeviceEntity deviceEntity = mapper.selectOne(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDeviceSn, entity.getDeviceSn()));
        // Update the information directly if the device already exists.
        if (deviceEntity != null) {
            entity.setId(deviceEntity.getId());
            mapper.updateById(entity);
            return Optional.of(deviceEntity);
        }
        return mapper.insert(entity) > 0 ? Optional.of(entity) : Optional.empty();
    }

    /**
     * Convert the received gateway device object into a database entity object.
     * @param gateway
     * @return
     */
    private DeviceEntity deviceGatewayConvertToDeviceEntity(StatusGatewayReceiver gateway) {
        if (gateway == null) {
            return new DeviceEntity();
        }
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        // Query the model information of this gateway device.
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                .getOneDictionaryInfoByTypeSubType(gateway.getType(), gateway.getSubType());

        dictionaryOpt.ifPresent(entity ->
                builder.deviceName(entity.getDeviceName())
                        .nickname(entity.getDeviceName())
                        .deviceDesc(entity.getDeviceDesc()));

        return builder
                .deviceSn(gateway.getSn())
                .subType(gateway.getSubType())
                .deviceType(gateway.getType())
                .version(gateway.getVersion())
                .domain(gateway.getDomain() != null ?
                        gateway.getDomain() : DeviceDomainEnum.GATEWAY.getVal())
                .build();
    }

    /**
     * Convert the received drone device object into a database entity object.
     * @param device
     * @return
     */
    private DeviceEntity subDeviceConvertToDeviceEntity(StatusSubDeviceReceiver device) {
        if (device == null) {
            return new DeviceEntity();
        }
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        // Query the model information of this drone device.
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                .getOneDictionaryInfoByTypeSubType(device.getType(), device.getSubType());

        dictionaryOpt.ifPresent(dictionary ->
                builder.deviceName(dictionary.getDeviceName())
                        .nickname(dictionary.getDeviceName())
                        .deviceDesc(dictionary.getDeviceDesc()));

        return builder
                .deviceSn(device.getSn())
                .deviceType(device.getType())
                .subType(device.getSubType())
                .version(device.getVersion())
                .deviceIndex(device.getIndex())
                .domain(device.getDomain() != null ?
                        device.getDomain() : DeviceDomainEnum.SUB_DEVICE.getVal())
                .build();
    }

    /**
     * Convert database entity object into device data transfer object.
     * @param entity
     * @return
     */
    private DeviceDTO deviceEntityConvertToDTO(DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        DeviceDTO.DeviceDTOBuilder deviceDTOBuilder = DeviceDTO.builder()
                .deviceSn(entity.getDeviceSn())
                .childDeviceSn(entity.getChildSn())
                .deviceName(entity.getDeviceName())
                .deviceDesc(entity.getDeviceDesc())
                .deviceIndex(entity.getDeviceIndex())
                .workspaceId(entity.getWorkspaceId())
                .type(entity.getDeviceType())
                .subType(entity.getSubType())
                .domain(DeviceDomainEnum.getDesc(entity.getDomain()))
                .iconUrl(IconUrlDTO.builder()
                        .normalUrl(entity.getUrlNormal())
                        .selectUrl(entity.getUrlSelect())
                        .build())
                .boundStatus(entity.getBoundStatus())
                .loginTime(entity.getLoginTime() != null ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getLoginTime()), ZoneId.systemDefault())
                        : null)
                .boundTime(entity.getBoundTime() != null ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getBoundTime()), ZoneId.systemDefault())
                        : null)
                .nickname(entity.getNickname())
                .firmwareVersion(entity.getFirmwareVersion())
                .workspaceName(entity.getWorkspaceId() != null ?
                        workspaceService.getWorkspaceByWorkspaceId(entity.getWorkspaceId())
                                .map(WorkspaceDTO::getWorkspaceName).orElse("") : "");

        if (!StringUtils.hasText(entity.getFirmwareVersion())) {
            return deviceDTOBuilder.firmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal()).build();
        }
        // Query whether the device is updating firmware.
        Object progress = redisOps.get(RedisConst.FIRMWARE_UPGRADING_PREFIX + entity.getDeviceSn());
        if (Objects.nonNull(progress)) {
            return deviceDTOBuilder.firmwareStatus(DeviceFirmwareStatusEnum.UPGRADING.getVal()).firmwareProgress((int)progress).build();
        }

        // First query the latest firmware version of the device model and compare it with the current firmware version
        // to see if it needs to be upgraded.
        Optional<DeviceFirmwareNoteDTO> firmwareReleaseNoteOpt = deviceFirmwareService.getLatestFirmwareReleaseNote(entity.getDeviceName());
        if (firmwareReleaseNoteOpt.isPresent()) {
            DeviceFirmwareNoteDTO firmwareNoteDTO = firmwareReleaseNoteOpt.get();
            if (firmwareNoteDTO.getProductVersion().equals(entity.getFirmwareVersion())) {
                return deviceDTOBuilder.firmwareStatus(entity.getCompatibleStatus() ?
                        DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal() :
                        DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE.getVal()).build();
            }

            return deviceDTOBuilder.firmwareStatus(DeviceFirmwareStatusEnum.NORMAL_UPGRADE.getVal()).build();
        }
        return deviceDTOBuilder.firmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal()).build();
    }

    @Override
    public Boolean updateDevice(DeviceDTO deviceDTO) {
        int update = mapper.update(this.deviceDTO2Entity(deviceDTO),
                new LambdaUpdateWrapper<DeviceEntity>().eq(DeviceEntity::getDeviceSn, deviceDTO.getDeviceSn()));
        return update > 0;
    }

    @Override
    public Boolean bindDevice(DeviceDTO device) {
        device.setBoundStatus(true);
        device.setBoundTime(LocalDateTime.now());

        boolean isUpd = this.saveDevice(this.deviceDTO2Entity(device)).isPresent();
        if (DeviceDomainEnum.DOCK.getDesc().equals(device.getDomain())) {
            return isUpd;
        }
        if (!isUpd) {
            return false;
        }

        String key = RedisConst.DEVICE_ONLINE_PREFIX + device.getDeviceSn();
        DeviceDTO redisDevice = (DeviceDTO)redisOps.get(key);
        redisDevice.setWorkspaceId(device.getWorkspaceId());
        redisOps.setWithExpire(key, redisDevice, RedisConst.DEVICE_ALIVE_SECOND);

        if (DeviceDomainEnum.GATEWAY.getDesc().equals(redisDevice.getDomain())) {
            this.pushDeviceOnlineTopo(webSocketManageService.getValueWithWorkspace(device.getWorkspaceId()),
                    device.getDeviceSn(), device.getDeviceSn());
        }
        if (DeviceDomainEnum.SUB_DEVICE.getDesc().equals(redisDevice.getDomain())) {
            DeviceDTO subDevice = this.getDevicesByParams(DeviceQueryParam.builder()
                    .childSn(device.getChildDeviceSn())
                    .build()).get(0);
            this.pushDeviceOnlineTopo(webSocketManageService.getValueWithWorkspace(device.getWorkspaceId()),
                    device.getDeviceSn(), subDevice.getDeviceSn());
        }
        this.subscribeTopicOnline(device.getDeviceSn());

        return true;
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS, outputChannel = ChannelName.OUTBOUND)
    public void bindStatus(CommonTopicReceiver receiver, MessageHeaders headers) {
        List<Map<String, String>> data = ((Map<String, List<Map<String, String>>>) receiver.getData()).get(MapKeyConst.DEVICES);
        String dockSn = data.get(0).get(MapKeyConst.SN);
        String droneSn = data.size() > 1 ? data.get(1).get(MapKeyConst.SN) : "null";

        Optional<DeviceDTO> dockOpt = this.getDeviceBySn(dockSn);
        Optional<DeviceDTO> droneOpt = this.getDeviceBySn(droneSn);

        List<BindStatusReceiver> bindStatusResult = new ArrayList<>();
        bindStatusResult.add(dockOpt.isPresent() ? this.dto2BindStatus(dockOpt.get()) :
                BindStatusReceiver.builder().sn(dockSn).isDeviceBindOrganization(false).build());
        if (data.size() > 1) {
            bindStatusResult.add(droneOpt.isPresent() ? this.dto2BindStatus(droneOpt.get()) :
                    BindStatusReceiver.builder().sn(droneSn).isDeviceBindOrganization(false).build());
        }

        messageSender.publish(headers.get(MqttHeaders.RECEIVED_TOPIC) + _REPLY_SUF,
                CommonTopicResponse.builder()
                        .tid(receiver.getTid())
                        .bid(receiver.getBid())
                        .timestamp(System.currentTimeMillis())
                        .method(RequestsMethodEnum.AIRPORT_BIND_STATUS.getMethod())
                        .data(RequestsReply.success(Map.of(MapKeyConst.BIND_STATUS, bindStatusResult)))
                        .build());

    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND)
    public void bindDevice(CommonTopicReceiver receiver, MessageHeaders headers) {
        Map<String, List<BindDeviceReceiver>> data = objectMapper.convertValue(receiver.getData(),
                new TypeReference<Map<String, List<BindDeviceReceiver>>>() {});
        List<BindDeviceReceiver> devices = data.get(MapKeyConst.BIND_DEVICES);
        BindDeviceReceiver dock = null;
        BindDeviceReceiver drone = null;
        for (BindDeviceReceiver device : devices) {
            int val = Integer.parseInt(device.getDeviceModelKey().split("-")[0]);
            if (val == DeviceDomainEnum.DOCK.getVal()) {
                dock = device;
            }
            if (val == DeviceDomainEnum.SUB_DEVICE.getVal()) {
                drone = device;
            }
        }

        assert dock != null;

        Optional<DeviceEntity> dockEntityOpt = this.bindDevice2Entity(dock);
        Optional<DeviceEntity> droneEntityOpt = this.bindDevice2Entity(drone);

        List<ErrorInfoReply> bindResult = new ArrayList<>();

        droneEntityOpt.ifPresent(droneEntity -> {
            dockEntityOpt.get().setChildSn(droneEntity.getDeviceSn());
            Optional<DeviceEntity> deviceEntityOpt = this.saveDevice(droneEntity);
            bindResult.add(
                    deviceEntityOpt.isPresent() ?
                        ErrorInfoReply.success(droneEntity.getDeviceSn()) :
                        new ErrorInfoReply(droneEntity.getDeviceSn(),
                                CommonErrorEnum.DEVICE_BINDING_FAILED.getErrorCode())
            );
        });
        Optional<DeviceEntity> dockOpt = this.saveDevice(dockEntityOpt.get());

        bindResult.add(dockOpt.isPresent() ?
                ErrorInfoReply.success(dock.getSn()) :
                    new ErrorInfoReply(dock.getSn(),
                            CommonErrorEnum.DEVICE_BINDING_FAILED.getErrorCode()));

        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC) + _REPLY_SUF;
        messageSender.publish(topic,
                CommonTopicResponse.builder()
                        .tid(receiver.getTid())
                        .bid(receiver.getBid())
                        .method(RequestsMethodEnum.AIRPORT_ORGANIZATION_BIND.getMethod())
                        .timestamp(System.currentTimeMillis())
                        .data(RequestsReply.success(Map.of(MapKeyConst.ERR_INFOS, bindResult)))
                        .build());

    }

    @Override
    public PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page,
                                                               Long pageSize, String domain) {

        Page<DeviceEntity> pagination = mapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDomain, DeviceDomainEnum.getVal(domain))
                        .eq(DeviceEntity::getWorkspaceId, workspaceId)
                        .eq(DeviceEntity::getBoundStatus, true));
        List<DeviceDTO> devicesList = pagination.getRecords().stream().map(this::deviceEntityConvertToDTO)
                .peek(device -> {
                    device.setStatus(redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + device.getDeviceSn()));
                    if (StringUtils.hasText(device.getChildDeviceSn())) {
                        Optional<DeviceDTO> childOpt = this.getDeviceBySn(device.getChildDeviceSn());
                        childOpt.ifPresent(child -> {
                            child.setStatus(
                                    redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + child.getDeviceSn()));
                            child.setWorkspaceName(device.getWorkspaceName());
                            device.setChildren(child);
                        });
                    }
                })
                .collect(Collectors.toList());
        return new PaginationData<DeviceDTO>(devicesList, new Pagination(pagination));
    }

    @Override
    public void unbindDevice(String deviceSn) {
        String key = RedisConst.DEVICE_ONLINE_PREFIX + deviceSn;
        DeviceDTO redisDevice = (DeviceDTO) redisOps.get(key);
        redisDevice.setWorkspaceId("");
        redisOps.setWithExpire(key, redisDevice, RedisConst.DEVICE_ALIVE_SECOND);

        DeviceDTO device = DeviceDTO.builder()
                .deviceSn(deviceSn)
                .workspaceId("")
                .userId("")
                .boundStatus(false)
                .build();
        this.updateDevice(device);
    }

    @Override
    public Optional<DeviceDTO> getDeviceBySn(String sn) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(DeviceQueryParam.builder().deviceSn(sn).build());
        if (devicesList.isEmpty()) {
            return Optional.empty();
        }
        DeviceDTO device = devicesList.get(0);
        device.setStatus(redisOps.checkExist(RedisConst.DEVICE_ONLINE_PREFIX + sn));
        return Optional.of(device);
    }

    @Override
    public void updateFirmwareVersion(FirmwareVersionReceiver receiver) {
        if (receiver.getDomain() == DeviceDomainEnum.SUB_DEVICE) {
            final DeviceDTO device = DeviceDTO.builder()
                    .deviceSn(receiver.getSn())
                    .firmwareVersion(receiver.getFirmwareVersion())
                    .firmwareStatus(receiver.getCompatibleStatus() == null ?
                            null : DeviceFirmwareStatusEnum.CompatibleStatusEnum.INCONSISTENT.getVal() != receiver.getCompatibleStatus() ?
                            DeviceFirmwareStatusEnum.UNKNOWN.getVal() : DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE.getVal())
                    .build();
            this.updateDevice(device);
            return;
        }
        payloadService.updateFirmwareVersion(receiver);
    }

    @Override
    public ResponseResult createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<DeviceOtaCreateParam> deviceOtaFirmwares = deviceFirmwareService.getDeviceOtaFirmware(upgradeDTOS);
        if (deviceOtaFirmwares.isEmpty()) {
            return ResponseResult.error();
        }

        DeviceOtaCreateParam deviceOtaFirmware = deviceOtaFirmwares.get(0);
        List<DeviceDTO> devices = getDevicesByParams(DeviceQueryParam.builder().childSn(deviceOtaFirmware.getSn()).build());
        String gatewaySn = devices.isEmpty() ? deviceOtaFirmware.getSn() : devices.get(0).getDeviceSn();

        String topic = THING_MODEL_PRE + PRODUCT + gatewaySn + SERVICES_SUF;

        // The bids in the progress messages reported subsequently are the same.
        String bid = UUID.randomUUID().toString();
        Optional<ServiceReply> serviceReplyOpt = messageSender.publishWithReply(
                topic, CommonTopicResponse.<Map<String, List<DeviceOtaCreateParam>>>builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(bid)
                        .timestamp(System.currentTimeMillis())
                        .method(ServicesMethodEnum.OTA_CREATE.getMethod())
                        .data(Map.of(MapKeyConst.DEVICES, deviceOtaFirmwares))
                        .build());
        if (serviceReplyOpt.isEmpty()) {
            return ResponseResult.error("No message reply received.");
        }
        ServiceReply serviceReply = serviceReplyOpt.get();
        if (serviceReply.getResult() != ResponseResult.CODE_SUCCESS) {
            return ResponseResult.error(serviceReply.getResult(), "Firmware Error Code: " + serviceReply.getResult());
        }
        if (ServicesMethodEnum.OTA_CREATE.getProgress()) {
            // Record the device state that needs to be updated.
            deviceOtaFirmwares.forEach(deviceOta -> redisOps.setWithExpire(
                    RedisConst.FIRMWARE_UPGRADING_PREFIX + deviceOta.getSn(),
                    bid,
                    RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND));
        }
        return ResponseResult.success();
    }

    /**
     * Convert device data transfer object into database entity object.
     * @param dto
     * @return
     */
    private DeviceEntity deviceDTO2Entity(DeviceDTO dto) {
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();
        if (dto == null) {
            return builder.build();
        }

        return builder.deviceSn(dto.getDeviceSn())
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .workspaceId(dto.getWorkspaceId())
                .boundStatus(dto.getBoundStatus())
                .loginTime(dto.getLoginTime() != null ?
                        dto.getLoginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .boundTime(dto.getBoundTime() != null ?
                        dto.getBoundTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .childSn(dto.getChildDeviceSn())
                .domain(StringUtils.hasText(dto.getDomain()) ? DeviceDomainEnum.getVal(dto.getDomain()) : null)
                .firmwareVersion(dto.getFirmwareVersion())
                .compatibleStatus(dto.getFirmwareStatus() == null ? null :
                        DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE != DeviceFirmwareStatusEnum.find(dto.getFirmwareStatus()))
                .build();
    }

    /**
     * Convert device binding data object into database entity object.
     * @param receiver
     * @return
     */
    private Optional<DeviceEntity> bindDevice2Entity(BindDeviceReceiver receiver) {
        if (receiver == null) {
            return Optional.empty();
        }
        int[] droneKey = Arrays.stream(receiver.getDeviceModelKey().split("-")).mapToInt(Integer::parseInt).toArray();
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService.getOneDictionaryInfoByTypeSubType(droneKey[1], droneKey[2]);
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        dictionaryOpt.ifPresent(entity ->
                builder.deviceName(entity.getDeviceName())
                        .nickname(entity.getDeviceName())
                        .deviceDesc(entity.getDeviceDesc()));

        Optional<WorkspaceDTO> workspace = workspaceService.getWorkspaceNameByBindCode(receiver.getDeviceBindingCode());

        DeviceEntity entity = builder
                .workspaceId(workspace.isPresent() ? workspace.get().getWorkspaceId() : receiver.getOrganizationId())
                .domain(droneKey[0])
                .deviceType(droneKey[1])
                .subType(droneKey[2])
                .deviceSn(receiver.getSn())
                .boundStatus(true)
                .loginTime(System.currentTimeMillis())
                .boundTime(System.currentTimeMillis())
                .urlSelect(IconUrlEnum.SELECT_EQUIPMENT.getUrl())
                .urlNormal(IconUrlEnum.NORMAL_EQUIPMENT.getUrl())
                .build();
        if (StringUtils.hasText(receiver.getDeviceCallsign())) {
            entity.setNickname(receiver.getDeviceCallsign());
        }
        return Optional.of(entity);
    }

    /**
     * Convert device data transfer object into device binding status data object.
     * @param device
     * @return
     */
    private BindStatusReceiver dto2BindStatus(DeviceDTO device) {
        if (device == null) {
            return null;
        }
        return BindStatusReceiver.builder()
                .sn(device.getDeviceSn())
                .deviceCallsign(device.getNickname())
                .isDeviceBindOrganization(device.getBoundStatus())
                .organizationId(device.getWorkspaceId())
                .organizationName(device.getWorkspaceName())
                .build();
    }
}