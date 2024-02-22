package com.dji.sample.control.service.impl;

import com.dji.sample.component.mqtt.model.EventsReceiver;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sdk.cloudapi.debug.RemoteDebugProgress;
import com.dji.sdk.cloudapi.debug.api.AbstractDebugService;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.events.EventsDataRequest;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sean
 * @version 1.7
 * @date 2023/7/4
 */
@Service
@Slf4j
public class SDKRemoteDebug extends AbstractDebugService {

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Override
    public TopicEventsResponse<MqttReply> remoteDebugProgress(TopicEventsRequest<EventsDataRequest<RemoteDebugProgress>> request, MessageHeaders headers) {
        String sn = request.getGateway();

        EventsReceiver<RemoteDebugProgress> eventsReceiver = new EventsReceiver<RemoteDebugProgress>()
                .setOutput(request.getData().getOutput()).setResult(request.getData().getResult());
        eventsReceiver.setBid(request.getBid());
        eventsReceiver.setSn(sn);

        log.info("SN: {}, {} ===> Control progress: {}", sn, request.getMethod(), eventsReceiver.getOutput().getProgress());

        if (!eventsReceiver.getResult().isSuccess()) {
            log.error("SN: {}, {} ===> Error: {}", sn, request.getMethod(), eventsReceiver.getResult());
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(sn);

        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("The device is offline.");
        }

        DeviceDTO device = deviceOpt.get();
        webSocketMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                request.getMethod(), eventsReceiver);

        return new TopicEventsResponse<MqttReply>().setData(MqttReply.success());
    }
}
