package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DevicePayloadDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.dto.TelemetryDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdPayloadReceiver;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
@Slf4j
public class DeviceOSDServiceImpl extends AbstractTSAService {

    protected DeviceOSDServiceImpl(@Autowired @Qualifier("dockOSDServiceImpl") AbstractTSAService tsaService) {
        super(tsaService);
    }

    @Override
    public void pushTelemetryData(Collection<ConcurrentWebSocketSession> sessions,
                                  CustomWebSocketMessage<TelemetryDTO> message, Object osdData) {
        if (osdData instanceof OsdSubDeviceReceiver) {
            OsdSubDeviceReceiver data = (OsdSubDeviceReceiver) osdData;
            TelemetryDTO telemetry = message.getData();
            telemetry.setHost(TelemetryDeviceDTO.builder()
                    .latitude(data.getLatitude())
                    .longitude(data.getLongitude())
                    .altitude(data.getElevation())
                    .attitudeHead(data.getAttitudeHead())
                    .elevation(data.getElevation())
                    .horizontalSpeed(data.getHorizontalSpeed())
                    .verticalSpeed(data.getVerticalSpeed())
                    .build());

            this.sendMessageService.sendBatch(sessions, message);
        }
    }
    @Override
    public void handleOSD(CommonTopicReceiver receiver, DeviceDTO device,
                          Collection<ConcurrentWebSocketSession> webSessions,
                          CustomWebSocketMessage<TelemetryDTO> wsMessage) {
        if (DeviceDomainEnum.SUB_DEVICE.getDesc().equals(device.getDomain())) {
            wsMessage.setBizCode(BizCodeEnum.DEVICE_OSD.getCode());

            OsdSubDeviceReceiver data = mapper.convertValue(receiver.getData(), OsdSubDeviceReceiver.class);
            List<DevicePayloadDTO> payloadsList = device.getPayloadsList();
            try {
                Map<String, Object> receiverData = (Map<String, Object>) receiver.getData();
                data.setPayloads(payloadsList.stream()
                        .map(payload -> mapper.convertValue(
                                receiverData.getOrDefault(payload.getPayloadName(), Map.of()),
                                OsdPayloadReceiver.class))
                        .collect(Collectors.toList()));

            } catch (NullPointerException e) {
                log.warn("Please remount the payload, or restart the drone. Otherwise the data of the payload will not be received.");
            }


            wsMessage.getData().setHost(data);

            sendMessageService.sendBatch(webSessions, wsMessage);
            this.pushTelemetryData(device.getWorkspaceId(), data, device.getDeviceSn());
        }
        tsaService.handleOSD(receiver, device, webSessions, wsMessage);
    }
}
