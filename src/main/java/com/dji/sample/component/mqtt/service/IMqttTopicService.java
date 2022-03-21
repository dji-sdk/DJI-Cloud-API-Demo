package com.dji.sample.component.mqtt.service;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public interface IMqttTopicService {

    /**
     * Subscribe to a specific topic.
     * @param topic target
     */
    void subscribe(@Header(MqttHeaders.TOPIC) String topic);

    /**
     * Subscribe to a specific topic using a specific qos.
     * @param topic target
     * @param qos   qos
     */
    void subscribe(@Header(MqttHeaders.TOPIC) String topic, int qos);

    /**
     * Unsubscribe from a specific topic.
     * @param topic target
     */
    void unsubscribe(@Header(MqttHeaders.TOPIC) String topic);

    /**
     * Get all the subscribed topics.
     * @return topics
     */
    String[] getSubscribedTopic();
}
