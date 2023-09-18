package com.dji.sample.component.mqtt.config;

import com.dji.sdk.mqtt.ChannelName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executor;

/**
 * Definition classes for all channels
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
public class MqttMessageChannel {

    @Autowired
    private Executor threadPool;

    @Bean(name = ChannelName.INBOUND)
    public MessageChannel inboundChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.DEFAULT)
    public MessageChannel defaultChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS)
    public MessageChannel statusChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE)
    public MessageChannel stateChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_SERVICES_REPLY)
    public MessageChannel serviceReplyChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_OSD)
    public MessageChannel osdChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS)
    public MessageChannel requestsChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS)
    public MessageChannel eventsChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_PROPERTY_SET_REPLY)
    public MessageChannel propertySetReply() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_DRC_UP)
    public MessageChannel drcUp() {
        return new DirectChannel();
    }

}
