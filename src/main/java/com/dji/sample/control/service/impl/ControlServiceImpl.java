package com.dji.sample.control.service.impl;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.control.model.enums.RemoteControlMethodEnum;
import com.dji.sample.control.model.param.RemoteDebugParam;
import com.dji.sample.control.service.IControlService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import com.dji.sample.manage.service.IDeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
@Service
@Slf4j
public class ControlServiceImpl implements IControlService {

    @Autowired
    private IMessageSenderService messageSenderService;

    @Autowired
    private ISendMessageService webSocketMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public ResponseResult controlDock(String sn, String serviceIdentifier, RemoteDebugParam param) {
        RemoteControlMethodEnum controlMethodEnum = RemoteControlMethodEnum.find(serviceIdentifier);
        if (RemoteControlMethodEnum.UNKNOWN == controlMethodEnum) {
            return ResponseResult.error("The " + serviceIdentifier + " method does not exist.");
        }

        Object data = "";
        // Add parameter validation.
        if (Objects.nonNull(controlMethodEnum.getClazz())) {
            if (Objects.isNull(param)) {
                return ResponseResult.error(CommonErrorEnum.ILLEGAL_ARGUMENT);
            }
            BasicDeviceProperty basicDeviceProperty = mapper.convertValue(param.getAction(), controlMethodEnum.getClazz());
            if (!basicDeviceProperty.valid()) {
                return ResponseResult.error(CommonErrorEnum.ILLEGAL_ARGUMENT);
            }
            data = basicDeviceProperty;
        }

        boolean isExist = deviceService.checkDeviceOnline(sn);
        if (!isExist) {
            return ResponseResult.error("The dock is offline.");
        }
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + sn + TopicConst.SERVICES_SUF;
        String bid = UUID.randomUUID().toString();
        ServiceReply serviceReplyOpt = messageSenderService.publishWithReply(
                topic, CommonTopicResponse.builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(bid)
                        .method(serviceIdentifier)
                        .timestamp(System.currentTimeMillis())
                        .data(data)
                        .build());

        ServiceReply<EventsOutputReceiver> serviceReply = mapper.convertValue(
                serviceReplyOpt, new TypeReference<ServiceReply<EventsOutputReceiver>>() {});
        if (ResponseResult.CODE_SUCCESS != serviceReply.getResult()) {
            return ResponseResult.error(serviceReply.getResult(),
                    Objects.nonNull(serviceReply.getOutput()) ? serviceReply.getOutput().getStatus()
                            : "error: " + serviceIdentifier + serviceReply.getResult());
        }
        if (controlMethodEnum.getProgress()) {
            RedisOpsUtils.setWithExpire(serviceIdentifier + RedisConst.DELIMITER +  bid, sn,
                    RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
        }
        return ResponseResult.success();
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, outputChannel = ChannelName.OUTBOUND)
    public void handleControlProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String key = receiver.getMethod() + RedisConst.DELIMITER + receiver.getBid();
        if (RedisOpsUtils.getExpire(key) <= 0) {
            return;
        }
        String sn = RedisOpsUtils.get(key).toString();

        EventsReceiver<EventsOutputReceiver> eventsReceiver = mapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<EventsOutputReceiver>>(){});
        eventsReceiver.setBid(receiver.getBid());
        eventsReceiver.setSn(sn);

        log.info("SN: {}, {} ===> Control progress: {}",
                sn, receiver.getMethod(), eventsReceiver.getOutput().getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("SN: {}, {} ===> Error code: {}", sn, receiver.getMethod(), eventsReceiver.getResult());
        }

        if (eventsReceiver.getOutput().getProgress().getPercent() == 100 ||
                EventsResultStatusEnum.find(eventsReceiver.getOutput().getStatus()).getEnd()) {
            RedisOpsUtils.del(key);
        }

        DeviceDTO device = (DeviceDTO) RedisOpsUtils.get(RedisConst.DEVICE_ONLINE_PREFIX + sn);
        webSocketMessageService.sendBatch(
                webSocketManageService.getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                CustomWebSocketMessage.builder()
                        .data(eventsReceiver)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(receiver.getMethod())
                        .build());

        if (receiver.getNeedReply() != null && receiver.getNeedReply() == 1) {
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF;
            messageSenderService.publish(topic,
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .method(receiver.getMethod())
                            .timestamp(System.currentTimeMillis())
                            .data(RequestsReply.success())
                            .build());
        }
    }
}
