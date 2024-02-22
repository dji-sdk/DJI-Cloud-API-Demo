package com.dji.sdk.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeHandler;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired(required = false)
    private HandshakeHandler handshakeHandler;

    @Autowired
    private WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Set the WebSocket connection address
        registry.addEndpoint("/api/v1/ws").setAllowedOriginPatterns("*")
                .setHandshakeHandler(handshakeHandler);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(webSocketHandlerDecoratorFactory);
    }

}