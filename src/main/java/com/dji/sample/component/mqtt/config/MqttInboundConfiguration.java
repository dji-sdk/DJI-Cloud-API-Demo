package com.dji.sample.component.mqtt.config;

import com.dji.sample.component.mqtt.model.ChannelName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

/**
 * Client configuration for inbound messages.
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttInboundConfiguration {

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    @Resource(name = ChannelName.INBOUND)
    private MessageChannel inboundChannel;

    /**
     * Clients of inbound message channels.
     * @return
     */
    @Bean(name = "adapter")
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttConfiguration.getClientId() + "_consumer_" + System.currentTimeMillis(),
                mqttClientFactory, mqttConfiguration.getInboundTopic().split(","));
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        // use byte types uniformly
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(1);
        adapter.setOutputChannel(inboundChannel);
        return adapter;
    }

    /**
     * Define a default channel to handle messages that have no effect.
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.DEFAULT)
    public MessageHandler defaultInboundHandler() {
        return message -> {
            log.info("The default channel does not handle messages." +
                    "\nTopic: " + message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC) +
                    "\nPayload: " + message.getPayload());
        };
    }

}
