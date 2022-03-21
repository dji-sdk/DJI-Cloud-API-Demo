package com.dji.sample.component.websocket.service;

import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;

import java.util.Collection;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
public interface ISendMessageService {

    /**
     * Send a message to the specific connection.
     * @param session   A WebSocket connection object
     * @param message   message
     */
    void sendMessage(ConcurrentWebSocketSession session, CustomWebSocketMessage message);

    /**
     * Send the same message to specific connection.
     * @param sessions  A collection of WebSocket connection objects.
     * @param message   message
     */
    void sendBatch(Collection<ConcurrentWebSocketSession> sessions, CustomWebSocketMessage message);
}
