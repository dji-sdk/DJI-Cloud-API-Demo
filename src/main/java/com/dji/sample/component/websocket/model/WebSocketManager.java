package com.dji.sample.component.websocket.model;

import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manage all WebSocket connection objects.
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Slf4j
public class WebSocketManager {

    private static final ConcurrentHashMap<String,
            ConcurrentHashMap<String,
                    ConcurrentHashMap<String, ConcurrentWebSocketSession>>> MANAGER = new ConcurrentHashMap<>(16);

    /**
     * WebSocket connection from the pilot.
     */
    private static final Set<ConcurrentWebSocketSession> PILOT_SESSION = ConcurrentHashMap.newKeySet(16);

    /**
     * WebSocket connection from the web.
     */
    private static final Set<ConcurrentWebSocketSession> WEB_SESSION = ConcurrentHashMap.newKeySet(16);

    public static void put(String key, ConcurrentWebSocketSession val) {

        String[] name = key.split("/");
        if (name.length != 3) {
            log.debug("The key is out of format. [{workspaceId}/{userType}/{userId}]");
            return;
        }

        ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentWebSocketSession>> workspaceSessions =
                MANAGER.getOrDefault(name[0], new ConcurrentHashMap<>(16));

        ConcurrentHashMap<String, ConcurrentWebSocketSession> userSessions = workspaceSessions.getOrDefault(
                name[2], new ConcurrentHashMap<>(16));
        userSessions.put(val.getId(), val);
        workspaceSessions.put(name[2], userSessions);
        MANAGER.put(name[0], workspaceSessions);

        getSetByUserType(Integer.valueOf(name[1])).add(val);

    }

    public static void remove(String key, String sessionId) {
        String[] name = key.split("/");
        if (name.length != 3) {
            log.debug("The key is out of format. [{workspaceId}/{userType}/{userId}]");
            return;
        }
        ConcurrentHashMap<String, ConcurrentWebSocketSession> userSession = MANAGER.get(name[0]).get(name[2]);

        Set<ConcurrentWebSocketSession> typeSession = getSetByUserType(Integer.valueOf(name[1]));

        ConcurrentWebSocketSession session = userSession.get(sessionId);
        typeSession.remove(session);
        userSession.remove(sessionId);
    }

    public static int getConnectedCount() {
        return PILOT_SESSION.size() + WEB_SESSION.size();
    }

    public static Collection<ConcurrentWebSocketSession> getValueWithWorkspace(String workspaceId) {
        Set<ConcurrentWebSocketSession> sessions = ConcurrentHashMap.newKeySet();

        MANAGER.get(workspaceId)
                .forEach((userId, userSessions) -> {
                    sessions.addAll(userSessions.values());
                });
        return sessions;
    }

    public static Collection<ConcurrentWebSocketSession> getValueWithWorkspaceAndUserType(String workspaceId, Integer userType) {
        Set<ConcurrentWebSocketSession> sessions = ConcurrentHashMap.newKeySet();
        Set<ConcurrentWebSocketSession> typeSessions = getSetByUserType(userType);

        MANAGER.getOrDefault(workspaceId, new ConcurrentHashMap<>())
                .forEach((userId, userSessions) -> {
                    Collection<ConcurrentWebSocketSession> sessionList = userSessions.values();
                    if (!sessionList.isEmpty()) {
                        ConcurrentWebSocketSession session = sessionList.iterator().next();
                        if (typeSessions.contains(session)) {
                            sessions.addAll(sessionList);
                        }
                    }
                });
        return sessions;
    }

    private static Set<ConcurrentWebSocketSession> getSetByUserType(Integer userType) {
        if (UserTypeEnum.PILOT.getVal() == userType) {
            return PILOT_SESSION;
        }

        if (UserTypeEnum.WEB.getVal() == userType) {
            return WEB_SESSION;
        }
        return new HashSet<>();
    }
}