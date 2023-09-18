package com.dji.sdk.mqtt;

import com.dji.sdk.common.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
public class InboundMessageRouter extends AbstractMessageRouter {

    private static final Logger log = LoggerFactory.getLogger(InboundMessageRouter.class);

    /**
     * All mqtt broker messages will arrive here before distributing them to different channels.
     * @param message message from mqtt broker
     * @return channel
     */
    @Override
    @Router(inputChannel = ChannelName.INBOUND)
    protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        byte[] payload = (byte[])message.getPayload();

        log.debug("received topic: {} \t payload =>{}", topic, new String(payload));

        CloudApiTopicEnum topicEnum = CloudApiTopicEnum.find(topic);
        MessageChannel bean = (MessageChannel) SpringBeanUtils.getBean(topicEnum.getBeanName());

        return Collections.singleton(bean);
    }
}
