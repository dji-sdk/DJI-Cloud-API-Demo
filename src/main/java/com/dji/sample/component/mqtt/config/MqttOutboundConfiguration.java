package com.dji.sample.component.mqtt.config;

import com.dji.sample.component.mqtt.model.ChannelName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

/**
 * Client configuration for outbound messages.
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
public class MqttOutboundConfiguration {

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    /**
     * Clients of outbound message channels.
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.OUTBOUND)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttConfiguration.getClientId() + "_producer_" + System.currentTimeMillis(),
                mqttClientFactory);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        // use byte types uniformly
        converter.setPayloadAsBytes(true);

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(0);
        messageHandler.setConverter(converter);
        return messageHandler;
    }
}
