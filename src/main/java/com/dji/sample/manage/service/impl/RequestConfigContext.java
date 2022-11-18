package com.dji.sample.manage.service.impl;

import com.dji.sample.common.util.SpringBeanUtils;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.manage.model.receiver.RequestConfigReceiver;
import com.dji.sample.manage.service.IRequestsConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Service
public class RequestConfigContext {

    @Autowired
    private IMessageSenderService messageSenderService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Handles the config method of the requests topic.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_CONFIG, outputChannel = ChannelName.OUTBOUND)
    void handleConfig(CommonTopicReceiver receiver, MessageHeaders headers) {
        RequestConfigReceiver configReceiver = objectMapper.convertValue(receiver.getData(), RequestConfigReceiver.class);
        Optional<ConfigScopeEnum> scopeEnumOpt = ConfigScopeEnum.find(configReceiver.getConfigScope());
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF;
        CommonTopicResponse.CommonTopicResponseBuilder<Object> build = CommonTopicResponse.builder()
                .tid(receiver.getTid())
                .bid(receiver.getBid())
                .timestamp(System.currentTimeMillis())
                .method(receiver.getMethod());
        if (scopeEnumOpt.isEmpty()) {
            messageSenderService.publish(topic, build.build());
            return;
        }

        IRequestsConfigService requestsConfigService = SpringBeanUtils.getBean(scopeEnumOpt.get().getClazz());
        build.data(requestsConfigService.getConfig());
        messageSenderService.publish(topic, build.build());
    }
}
