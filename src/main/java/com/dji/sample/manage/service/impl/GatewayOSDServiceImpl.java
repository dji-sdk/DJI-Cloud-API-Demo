package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.DeviceStatusManager;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.dto.TelemetryDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdGatewayReceiver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
@Service
public class GatewayOSDServiceImpl extends AbstractTSAService {

    public GatewayOSDServiceImpl(@Autowired @Qualifier("deviceOSDServiceImpl") AbstractTSAService tsaService) {
        super(tsaService);
    }

    @Override
    public void pushTelemetryData(Collection<ConcurrentWebSocketSession> sessions,
                                  CustomWebSocketMessage<TelemetryDTO> message, Object osdData) {
        if (osdData instanceof OsdGatewayReceiver) {
            OsdGatewayReceiver data = (OsdGatewayReceiver) osdData;
            TelemetryDTO telemetry = message.getData();
            telemetry.setHost(TelemetryDeviceDTO.builder()
                    .latitude(data.getLatitude())
                    .longitude(data.getLongitude())
                    .build());
            this.sendMessageService.sendBatch(sessions, message);
            return;
        }
        tsaService.pushTelemetryData(sessions, message, osdData);
    }

    @Override
    protected void handleOSD(TopicStateReceiver receiver, String sn, String workspaceId, JsonNode hostNode,
                             Collection<ConcurrentWebSocketSession> webSessions, CustomWebSocketMessage wsMessage) throws JsonProcessingException {
        if (sn.equals(receiver.getGateway())) {
            // Real-time update of device status in memory
            DeviceStatusManager.STATUS_MANAGER.put(DeviceDomainEnum.GATEWAY.getVal() + "/" + sn,
                    LocalDateTime.now());

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            wsMessage.setBizCode(BizCodeEnum.GATEWAY_OSD.getCode());
            OsdGatewayReceiver data = mapper.treeToValue(hostNode, OsdGatewayReceiver.class);
            wsMessage.setData(data);

            this.sendMessageService.sendBatch(webSessions, wsMessage);

            this.pushTelemetryData(workspaceId, data, sn);
            return;
        }

        tsaService.handleOSD(receiver, sn, workspaceId, hostNode, webSessions, wsMessage);
    }
}
