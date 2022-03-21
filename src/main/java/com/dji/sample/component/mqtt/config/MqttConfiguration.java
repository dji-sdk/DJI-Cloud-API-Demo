package com.dji.sample.component.mqtt.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfiguration {

    private String protocol;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String clientId;

    /**
     * The topic to subscribe to immediately when client connects.
     */
    private String inboundTopic;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{
                new StringBuilder()
                        .append(protocol.trim())
                        .append("://")
                        .append(host.trim())
                        .append(":")
                        .append(port)
                        .toString()});
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setKeepAliveInterval(10);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }
}
