package com.dji.sample.component.websocket.service;

import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;

import java.util.Collection;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/25
 */
public interface IWebSocketManageService {

    void put(String key, ConcurrentWebSocketSession val);

    void remove(String key, String sessionId);

    Collection<ConcurrentWebSocketSession> getValueWithWorkspace(String workspaceId);

    Collection<ConcurrentWebSocketSession> getValueWithWorkspaceAndUserType(String workspaceId, Integer userType);

    Long getConnectedCount();
}
