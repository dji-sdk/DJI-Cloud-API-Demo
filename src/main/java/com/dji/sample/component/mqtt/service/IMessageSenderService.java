package com.dji.sample.component.mqtt.service;

import com.dji.sample.component.mqtt.model.CommonTopicResponse;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public interface IMessageSenderService {

    /**
     * Publish a message to a specific topic.
     * @param topic target
     * @param response message
     */
    void publish(String topic, CommonTopicResponse response);

    /**
     * Use a specific qos to push messages to a specific topic.
     * @param topic target
     * @param qos   qos
     * @param response  message
     */
    void publish(String topic, int qos, CommonTopicResponse response);

}
