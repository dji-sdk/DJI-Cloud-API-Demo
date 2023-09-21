package com.dji.sample.component.websocket.config;

import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sdk.websocket.WebSocketDefaultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

/**
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Component
@Primary
public class MyWebSocketFactory extends WebSocketDefaultFactory {

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new MyWebSocketHandler(handler, webSocketManageService);
    }
}