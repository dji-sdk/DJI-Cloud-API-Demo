package com.dji.sample.manage.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@MessageEndpoint
@Configuration
public class StateSplitter {

    /**
     * Split the state message data to different channels for handling according to their different types.
     * @param receiver state message
     * @return
     */
    @Splitter(inputChannel = ChannelName.INBOUND_STATE_SPLITTER, outputChannel = ChannelName.INBOUND_STATE_ROUTER)
    public Collection<Object> splitState(TopicStateReceiver receiver) {
        ArrayList<Object> type = new ArrayList<>();
        type.add(receiver.getData());
        return type;
    }

    /**
     * Split according to the different types in the list.
     * @return
     */
    @Bean
    public IntegrationFlow splitList() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_STATE_LIST)
                .split()
                .<Object, String> route(dataType -> {
                    Class<?> clazz = dataType.getClass();
                    if (DevicePayloadReceiver.class.isAssignableFrom(clazz)) {
                        return ChannelName.INBOUND_STATE_PAYLOAD;
                    }
                    return null;
                }, mapping -> {
                    mapping.channelMapping(ChannelName.INBOUND_STATE_PAYLOAD,
                            ChannelName.INBOUND_STATE_PAYLOAD);
                })
                .get();
    }
}