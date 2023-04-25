package com.dji.sample.component.websocket.service.impl;

import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
@Service
@Slf4j
public class SendMessageServiceImpl implements ISendMessageService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Override
    public void sendMessage(ConcurrentWebSocketSession session, CustomWebSocketMessage message) {
        if (session == null) {
            return;
        }

        try {
            if (!session.isOpen()) {
                session.close();
                log.debug("This session is closed.");
                return;
            }


            session.sendMessage(new TextMessage(mapper.writeValueAsBytes(message)));
        } catch (IOException e) {
            log.info("Failed to publish the message. {}", message.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void sendBatch(Collection<ConcurrentWebSocketSession> sessions, CustomWebSocketMessage message) {
        if (sessions.isEmpty()) {
            return;
        }

        try {

            TextMessage data = new TextMessage(mapper.writeValueAsBytes(message));

            for (ConcurrentWebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    session.close();
                    log.debug("This session is closed.");
                    return;
                }
                session.sendMessage(data);
            }

        } catch (IOException e) {
            log.info("Failed to publish the message. {}", message.toString());

            e.printStackTrace();
        }
    }

    @Override
    public void sendBatch(String workspaceId, Integer userType, String bizCode, Object data) {
        if (!StringUtils.hasText(workspaceId)) {
            throw new RuntimeException("Workspace ID does not exist.");
        }
        Collection<ConcurrentWebSocketSession> sessions = Objects.isNull(userType) ?
                webSocketManageService.getValueWithWorkspace(workspaceId) :
                webSocketManageService.getValueWithWorkspaceAndUserType(workspaceId, userType);

        this.sendBatch(sessions, CustomWebSocketMessage.builder()
                        .data(data)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(bizCode)
                        .build());
    }

    @Override
    public void sendBatch(String workspaceId, String bizCode, Object data) {
        this.sendBatch(workspaceId, null, bizCode, data);
    }
}