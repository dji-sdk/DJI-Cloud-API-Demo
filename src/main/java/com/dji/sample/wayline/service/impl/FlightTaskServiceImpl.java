package com.dji.sample.wayline.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.wayline.model.dto.FlightTaskProgressReceiver;
import com.dji.sample.wayline.service.IFlightTaskService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Service
@Slf4j
public class FlightTaskServiceImpl implements IFlightTaskService {

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ISendMessageService websocketMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private RedisOpsUtils redisOps;

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS, outputChannel = ChannelName.OUTBOUND)
    public void handleProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        EventsReceiver<FlightTaskProgressReceiver> eventsReceiver = mapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<FlightTaskProgressReceiver>>(){});
        eventsReceiver.setBid(receiver.getBid());

        log.info("Task progress: " + eventsReceiver.getOutput().getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("Error code: " + eventsReceiver.getResult());
        }

        DeviceDTO device = (DeviceDTO) redisOps.get(RedisConst.DEVICE_ONLINE_PREFIX + receiver.getGateway());
        websocketMessageService.sendBatch(
                webSocketManageService.getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                CustomWebSocketMessage.builder()
                        .data(eventsReceiver)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.FLIGHT_TASK_PROGRESS.getCode())
                        .build());

        if (receiver.getNeedReply() == 1) {
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF;
            messageSender.publish(topic,
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .method(EventsMethodEnum.FLIGHT_TASK_PROGRESS.getMethod())
                            .timestamp(System.currentTimeMillis())
                            .data(ResponseResult.success())
                            .build());
        }
    }
}
