package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Configuration
public class EventsRouter {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IMessageSenderService messageSenderService;

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
                        mapping -> Arrays.stream(EventsMethodEnum.values()).forEach(
                                methodEnum -> mapping.channelMapping(methodEnum, methodEnum.getChannelName())))
                .get();
    }

    @ServiceActivator(inputChannel = ChannelName.OUTBOUND_EVENTS, outputChannel = ChannelName.OUTBOUND)
    public void replyEventsOutbound(CommonTopicReceiver receiver, MessageHeaders headers) {
        if (Optional.ofNullable(receiver).map(CommonTopicReceiver::getNeedReply).flatMap(val -> Optional.of(1 != val)).orElse(true)) {
            return;
        }
        messageSenderService.publish(headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF,
                CommonTopicResponse.builder()
                        .tid(receiver.getTid())
                        .bid(receiver.getBid())
                        .method(receiver.getMethod())
                        .timestamp(System.currentTimeMillis())
                        .data(RequestsReply.success())
                        .build());

    }

}
