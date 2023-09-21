package com.dji.sdk.websocket.api;

import com.dji.sdk.common.Common;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.websocket.ConcurrentWebSocketSession;
import com.dji.sdk.websocket.WebSocketMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collection;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
public class WebSocketMessageSend {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageSend.class);

    public void sendMessage(ConcurrentWebSocketSession session, WebSocketMessageResponse message) {
        if (session == null) {
            return;
        }

        try {
            if (!session.isOpen()) {
                session.close();
                log.info("This session is closed.");
                return;
            }

            session.sendMessage(new TextMessage(Common.getObjectMapper().writeValueAsBytes(message)));
        } catch (IOException e) {
            throw new CloudSDKException(CloudSDKErrorEnum.WEBSOCKET_PUBLISH_ABNORMAL, e.getLocalizedMessage());
        }
    }

    public void sendBatch(Collection<ConcurrentWebSocketSession> sessions, WebSocketMessageResponse message) {
        if (sessions.isEmpty()) {
            return;
        }

        try {

            TextMessage data = new TextMessage(Common.getObjectMapper().writeValueAsBytes(message));

            for (ConcurrentWebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    session.close();
                    log.info("This session is closed.");
                    return;
                }
                session.sendMessage(data);
            }

        } catch (IOException e) {
            throw new CloudSDKException(CloudSDKErrorEnum.WEBSOCKET_PUBLISH_ABNORMAL, e.getLocalizedMessage());
        }
    }
}