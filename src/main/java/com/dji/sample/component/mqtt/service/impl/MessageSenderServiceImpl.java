package com.dji.sample.component.mqtt.service.impl;

import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttMessageGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;
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
        this.publish(topic, 1, response);
    }

    public void publish(String topic, int qos, CommonTopicResponse response) {
        try {
            log.info("send topic: {}, payload: {}", topic, response.toString());
            messageGateway.publish(topic, mapper.writeValueAsBytes(response), qos);
        } catch (JsonProcessingException e) {
            log.info("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }

    public <T> T publishWithReply(Class<T> clazz, String topic, CommonTopicResponse response) {
        return this.publishWithReply(clazz, topic, response, 2);
    }

    public <T> T publishWithReply(Class<T> clazz, String topic, CommonTopicResponse response, int retryTime) {
        AtomicInteger time = new AtomicInteger(0);
        // Retry three times
        while (time.getAndIncrement() <= retryTime) {
            this.publish(topic, response);

            Chan<CommonTopicReceiver<T>> chan = Chan.getInstance();
            // If the message is not received in 0.5 seconds then resend it again.
            CommonTopicReceiver<T> receiver = chan.get(response.getTid());

            // Need to match tid and bid.
            if (Objects.nonNull(receiver) && receiver.getTid().equals(response.getTid()) &&
                    receiver.getBid().equals(response.getBid())) {
                if (clazz.isAssignableFrom(receiver.getData().getClass())) {
                    return receiver.getData();
                }
                throw new TypeMismatchException(receiver.getData(), clazz);
            }
            // It must be guaranteed that the tid and bid of each message are different.
            response.setBid(UUID.randomUUID().toString());
            response.setTid(UUID.randomUUID().toString());
        }
        throw new RuntimeException("No message reply received.");
    }

    @Override
    public <T> ServiceReply<T> publishServicesTopic(TypeReference<T> clazz, String sn, String method, Object data, String bid) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + sn + TopicConst.SERVICES_SUF;
        ServiceReply reply = this.publishWithReply(ServiceReply.class, topic,
                CommonTopicResponse.builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(StringUtils.hasText(bid) ? bid : UUID.randomUUID().toString())
                        .timestamp(System.currentTimeMillis())
                        .method(method)
                        .data(Objects.requireNonNullElse(data, ""))
                        .build());
        if (Objects.isNull(clazz)) {
            return reply;
        }
        // put together in "output"
        if (Objects.nonNull(reply.getInfo())) {
            reply.setOutput(mapper.convertValue(reply.getInfo(), clazz));
        }
        if (Objects.nonNull(reply.getOutput())) {
            reply.setOutput(mapper.convertValue(reply.getOutput(), clazz));
        }
        return reply;
    }

    @Override
    public ServiceReply publishServicesTopic(String sn, String method, Object data, String bid) {
        return this.publishServicesTopic(null, sn, method, data, bid);
    }

    @Override
    public <T> ServiceReply<T> publishServicesTopic(TypeReference<T> clazz, String sn, String method, Object data) {
        return this.publishServicesTopic(clazz, sn, method, data, null);
    }

    @Override
    public ServiceReply publishServicesTopic(String sn, String method, Object data) {
        return this.publishServicesTopic(null, sn, method, data, null);
    }

}