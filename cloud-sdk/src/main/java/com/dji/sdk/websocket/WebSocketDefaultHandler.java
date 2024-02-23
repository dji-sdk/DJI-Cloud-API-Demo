package com.dji.sdk.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
public class WebSocketDefaultHandler extends WebSocketHandlerDecorator {

    private static final Logger log = LoggerFactory.getLogger(WebSocketDefaultHandler.class);

    public WebSocketDefaultHandler(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("{} is connected.", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("{} is disconnected.", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("received message: {}, from: {}", message.getPayload(), session.getId());
    }

}