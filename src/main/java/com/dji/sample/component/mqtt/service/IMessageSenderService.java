package com.dji.sample.component.mqtt.service;

import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.model.ServiceReply;

import java.util.Optional;

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

    /**
     * Send live streaming start message and receive a response at the same time.
     * @param topic
     * @param response  notification of whether the start is successful.
     * @return
     */
    Optional<ServiceReply> publishWithReply(String topic, CommonTopicResponse response);

    /**
     * Send live streaming start message and receive a response at the same time.
     * @param clazz
     * @param topic
     * @param response
     * @param retryTime
     * @param <T>
     * @return
     */
    <T> Optional<T> publishWithReply(Class<T> clazz, String topic, CommonTopicResponse response, int retryTime);
}
