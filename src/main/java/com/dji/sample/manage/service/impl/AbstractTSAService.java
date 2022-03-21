package com.dji.sample.manage.service.impl;

import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.model.WebSocketManager;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.ITSAService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
public abstract class AbstractTSAService implements ITSAService {

    protected AbstractTSAService tsaService;

    public AbstractTSAService(AbstractTSAService tsaService) {
        this.tsaService = tsaService;
    }

    @Autowired
    protected ISendMessageService sendMessageService;

    @Override
    public void pushTelemetryData(String workspaceId, Object osdData, String sn) {
        // All connected accounts on the pilot side of this workspace.
        Collection<ConcurrentWebSocketSession> pilotSessions = WebSocketManager
                .getValueWithWorkspaceAndUserType(
                        workspaceId, UserTypeEnum.PILOT.getVal());

        TelemetryDTO telemetry = TelemetryDTO.builder()
                .sn(sn)
                .build();
        CustomWebSocketMessage<TelemetryDTO> pilotMessage = CustomWebSocketMessage.<TelemetryDTO>builder()
                .timestamp(System.currentTimeMillis())
                .bizCode(BizCodeEnum.DEVICE_OSD.getCode())
                .data(telemetry)
                .build();

        this.pushTelemetryData(pilotSessions, pilotMessage, osdData);
    }

    public abstract void pushTelemetryData(Collection<ConcurrentWebSocketSession> sessions,
                                           CustomWebSocketMessage<TelemetryDTO> message, Object Object);

    protected abstract void handleOSD(TopicStateReceiver receiver, String sn, String workspaceId, JsonNode hostNode,
                                      Collection<ConcurrentWebSocketSession> webSessions, CustomWebSocketMessage wsMessage)
            throws JsonProcessingException;
}
