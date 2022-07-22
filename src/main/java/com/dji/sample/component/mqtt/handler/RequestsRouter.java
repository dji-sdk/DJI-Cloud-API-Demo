package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.RequestsMethodEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.io.IOException;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
@Configuration
public class RequestsRouter {

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public IntegrationFlow requestsMethodRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_REQUESTS)
                .<byte[], CommonTopicReceiver>transform(payload -> {
                    try {
                        return mapper.readValue(payload, CommonTopicReceiver.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new CommonTopicReceiver();
                })
                .<CommonTopicReceiver, RequestsMethodEnum>route(
                        receiver -> RequestsMethodEnum.find(receiver.getMethod()),
                        mapping -> {
                            mapping.channelMapping(RequestsMethodEnum.STORAGE_CONFIG_GET, ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET);
                            mapping.channelMapping(RequestsMethodEnum.AIRPORT_BIND_STATUS, ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS);
                            mapping.channelMapping(RequestsMethodEnum.AIRPORT_ORGANIZATION_GET, ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET);
                            mapping.channelMapping(RequestsMethodEnum.AIRPORT_ORGANIZATION_BIND, ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND);
                            mapping.channelMapping(RequestsMethodEnum.UNKNOWN, ChannelName.DEFAULT);
                        })
                .get();
    }
}
