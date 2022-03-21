package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.DeviceStatusManager;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.dto.TelemetryDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
public class DeviceOSDServiceImpl extends AbstractTSAService {

    protected DeviceOSDServiceImpl() {
        super(null);
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
    protected void handleOSD(TopicStateReceiver receiver, String sn, String workspaceId, JsonNode hostNode,
                             Collection<ConcurrentWebSocketSession> webSessions, CustomWebSocketMessage wsMessage) throws JsonProcessingException {
        // Real-time update of device status in memory
        DeviceStatusManager.STATUS_MANAGER.put(
                DeviceDomainEnum.SUB_DEVICE.getVal() + "/" + sn, LocalDateTime.now());
        wsMessage.setBizCode(BizCodeEnum.DEVICE_OSD.getCode());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OsdSubDeviceReceiver data = mapper.treeToValue(hostNode, OsdSubDeviceReceiver.class);

        wsMessage.setData(data);

        sendMessageService.sendBatch(webSessions, wsMessage);
        this.pushTelemetryData(workspaceId, data, sn);
    }
}
