package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.EventsMethodEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.io.IOException;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Configuration
public class EventsRouter {

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public IntegrationFlow eventsMethodRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_EVENTS)
                .<byte[], CommonTopicReceiver>transform(payload -> {
                    try {
                        return mapper.readValue(payload, CommonTopicReceiver.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new CommonTopicReceiver();
                })
                .<CommonTopicReceiver, EventsMethodEnum>route(
                        receiver -> EventsMethodEnum.find(receiver.getMethod()),
                        mapping -> {
                            mapping.channelMapping(EventsMethodEnum.FILE_UPLOAD_CALLBACK, ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK);
                            mapping.channelMapping(EventsMethodEnum.FLIGHT_TASK_PROGRESS, ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS);
                            mapping.channelMapping(EventsMethodEnum.HMS, ChannelName.INBOUND_EVENTS_HMS);
                            mapping.channelMapping(EventsMethodEnum.UNKNOWN, ChannelName.DEFAULT);
                        })
                .get();
    }
}
