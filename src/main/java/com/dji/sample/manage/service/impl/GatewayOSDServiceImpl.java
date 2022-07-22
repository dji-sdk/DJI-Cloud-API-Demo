package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.dto.TelemetryDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdGatewayReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    public void handleOSD(CommonTopicReceiver receiver, DeviceDTO device,
                          Collection<ConcurrentWebSocketSession> webSessions,
                          CustomWebSocketMessage<TelemetryDTO> wsMessage) {
        if (DeviceDomainEnum.GATEWAY.getDesc().equals(device.getDomain())) {

            wsMessage.setBizCode(BizCodeEnum.GATEWAY_OSD.getCode());
            OsdGatewayReceiver data = mapper.convertValue(receiver.getData(), OsdGatewayReceiver.class);
            wsMessage.getData().setHost(data);

            this.sendMessageService.sendBatch(webSessions, wsMessage);

            this.pushTelemetryData(device.getWorkspaceId(), data, device.getDeviceSn());
            return;
        }

        tsaService.handleOSD(receiver, device, webSessions, wsMessage);
    }
}
