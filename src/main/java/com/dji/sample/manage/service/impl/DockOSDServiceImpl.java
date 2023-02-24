package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.OsdDockReceiver;
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

        if (DeviceDomainEnum.DOCK.getVal() == device.getDomain()) {
            wsMessage.setBizCode(BizCodeEnum.DOCK_OSD.getCode());
            OsdDockReceiver data = mapper.convertValue(receiver.getData(), OsdDockReceiver.class);
            wsMessage.getData().setHost(data);
            sendMessageService.sendBatch(webSessions, wsMessage);
        }
    }
}
