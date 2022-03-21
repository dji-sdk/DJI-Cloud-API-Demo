package com.dji.sample.component.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketMessageConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private AuthPrincipalHandler authPrincipalHandler;

    @Autowired
    private WebSocketDefaultFactory webSocketDefaultFactory;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Set the WebSocket connection address
        registry.addEndpoint("/api/v1/ws").setAllowedOriginPatterns("*")
                .setHandshakeHandler(authPrincipalHandler);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(webSocketDefaultFactory);
        registry.setTimeToFirstMessage(60000 * 60 * 24 * 10);
    }

}