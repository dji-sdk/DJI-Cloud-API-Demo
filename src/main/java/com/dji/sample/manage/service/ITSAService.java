package com.dji.sample.manage.service;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;

import java.util.Collection;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
public interface ITSAService {

    /**
     * Real-time push telemetry data.
     * @param workspaceId
     * @param osdData
     * @param sn
     */
    void pushTelemetryData(String workspaceId, Object osdData, String sn);

    /**
     * Handle device's osd data.
     * @param receiver
     * @param webSessions
     * @param wsMessage
     */
    void handleOSD(CommonTopicReceiver receiver, DeviceDTO device,
                   Collection<ConcurrentWebSocketSession> webSessions, CustomWebSocketMessage<TelemetryDTO> wsMessage);

}
