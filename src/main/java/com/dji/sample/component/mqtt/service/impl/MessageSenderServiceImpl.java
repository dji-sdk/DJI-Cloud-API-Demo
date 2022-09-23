package com.dji.sample.component.mqtt.service.impl;

import com.dji.sample.component.mqtt.model.Chan;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.model.ServiceReply;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttMessageGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Service
@Slf4j
public class MessageSenderServiceImpl implements IMessageSenderService {

    @Autowired
    private IMqttMessageGateway messageGateway;

    @Autowired
    private ObjectMapper mapper;

    public void publish(String topic, CommonTopicResponse response) {
        try {

            messageGateway.publish(topic, mapper.writeValueAsBytes(response));
        } catch (JsonProcessingException e) {
            log.info("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }

    public void publish(String topic, int qos, CommonTopicResponse response) {
        try {
            messageGateway.publish(topic, mapper.writeValueAsBytes(response), qos);
        } catch (JsonProcessingException e) {
            log.info("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }

    public Optional<ServiceReply> publishWithReply(String topic, CommonTopicResponse response) {
        return this.publishWithReply(ServiceReply.class, topic, response, 2);
    }

    public <T> Optional<T> publishWithReply(Class<T> clazz, String topic, CommonTopicResponse response, int retryTime) {
        AtomicInteger time = new AtomicInteger(0);
        // Retry three times
        while (time.getAndIncrement() < retryTime) {
            this.publish(topic, response);

            Chan<CommonTopicReceiver<T>> chan = Chan.getInstance();
            // If the message is not received in 0.5 seconds then resend it again.
            CommonTopicReceiver<T> receiver = chan.get(response.getMethod());
            if (receiver == null) {
                continue;
            }
            // Need to match tid and bid.
            if (receiver.getTid().equals(response.getTid()) &&
                    receiver.getBid().equals(response.getBid())) {
                return Optional.ofNullable(receiver.getData());
            }
        }
        return Optional.empty();
    }
}