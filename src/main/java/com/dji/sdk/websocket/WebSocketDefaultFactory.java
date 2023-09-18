package com.dji.sdk.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Component
public class WebSocketDefaultFactory implements WebSocketHandlerDecoratorFactory {

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketDefaultHandler(handler);
    }
}