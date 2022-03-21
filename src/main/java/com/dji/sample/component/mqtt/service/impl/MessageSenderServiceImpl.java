package com.dji.sample.component.mqtt.service.impl;

import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttMessageGateway;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void publish(String topic, CommonTopicResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Only parameters whose value is not null will be serialised.
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            messageGateway.publish(topic, mapper.writeValueAsBytes(response));
        } catch (JsonProcessingException e) {
            log.info("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }

    public void publish(String topic, int qos, CommonTopicResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Only parameters whose value is not null will be serialised.
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            messageGateway.publish(topic, mapper.writeValueAsBytes(response), qos);
        } catch (JsonProcessingException e) {
            log.info("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }
}