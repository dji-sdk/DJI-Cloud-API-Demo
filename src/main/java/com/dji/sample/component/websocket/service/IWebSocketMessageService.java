package com.dji.sample.component.websocket.service;

import com.dji.sample.component.websocket.config.MyConcurrentWebSocketSession;
import com.dji.sdk.websocket.WebSocketMessageResponse;

import java.util.Collection;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
public interface IWebSocketMessageService {

    /**
     * Send a message to the specific connection.
     * @param session   A WebSocket connection object
     * @param message   message
     */
    void sendMessage(MyConcurrentWebSocketSession session, WebSocketMessageResponse message);

    /**
     * Send the same message to specific connection.
     * @param sessions  A collection of WebSocket connection objects.
     * @param message   message
     */
    void sendBatch(Collection<MyConcurrentWebSocketSession> sessions, WebSocketMessageResponse message);

    void sendBatch(String workspaceId, Integer userType, String bizCode, Object data);

    void sendBatch(String workspaceId, String bizCode, Object data);
}
