package com.dji.sample.component.websocket.config;

import com.dji.sample.component.websocket.service.IWebSocketManageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketDefaultHandler(handler, webSocketManageService);
    }
}