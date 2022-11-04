package com.dji.sample.component.mqtt.handler;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.DeviceTopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
@Slf4j
public class InboundMessageRouter extends AbstractMessageRouter {

    @Autowired
    private ApplicationContext applicationContext;

    private static final ConcurrentHashMap<String, MessageChannel> channels = new ConcurrentHashMap<>(16);

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

        log.debug("received topic :{} \t payload :{}", topic, new String(payload));

        DeviceTopicEnum topicEnum = DeviceTopicEnum.find(topic);
        if (channels.containsKey(topicEnum.getBeanName())) {
            return Collections.singleton(channels.get(topicEnum.getBeanName()));
        }

        MessageChannel bean = (MessageChannel) applicationContext.getBean(topicEnum.getBeanName());
        channels.put(topicEnum.getBeanName(), bean);
        return Collections.singleton(bean);
    }
}
