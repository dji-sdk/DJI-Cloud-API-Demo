package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdDockReceiver;
import com.dji.sample.manage.model.receiver.OsdDockTransmissionReceiver;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/11
 */
@Service
public class DockOSDServiceImpl extends AbstractTSAService {

    public DockOSDServiceImpl() {
        super(null);
    }

    @Override
    public void pushTelemetryData(Collection<ConcurrentWebSocketSession> sessions,
                                  CustomWebSocketMessage<TelemetryDTO> message, Object Object) {

    }

    @Override
    public void handleOSD(CommonTopicReceiver receiver, DeviceDTO device,
                          Collection<ConcurrentWebSocketSession> webSessions,
                          CustomWebSocketMessage<TelemetryDTO> wsMessage) {

        if (DeviceDomainEnum.DOCK.getDesc().equals(device.getDomain())) {
            wsMessage.setBizCode(BizCodeEnum.DOCK_OSD.getCode());
            OsdDockReceiver data = mapper.convertValue(receiver.getData(), OsdDockReceiver.class);
            wsMessage.getData().setHost(data);
            if (data.getSubDevice() == null) {
                OsdDockTransmissionReceiver transmission = mapper.convertValue(receiver.getData(), OsdDockTransmissionReceiver.class);
                wsMessage.getData().setHost(transmission);
            }
            sendMessageService.sendBatch(webSessions, wsMessage);
        }
    }
}
