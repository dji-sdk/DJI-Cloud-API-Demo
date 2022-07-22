package com.dji.sample.component.websocket.service.impl;

import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collection;

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
}