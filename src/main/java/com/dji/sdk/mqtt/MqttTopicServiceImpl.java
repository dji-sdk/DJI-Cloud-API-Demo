package com.dji.sdk.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
public class MqttTopicServiceImpl implements IMqttTopicService {

    private static final Logger log = LoggerFactory.getLogger(MqttTopicServiceImpl.class);

    @Resource
    private MqttPahoMessageDrivenChannelAdapter adapter;

    @Override
    public void subscribe(String... topics) {
        Set<String> topicSet = new HashSet<>(Arrays.asList(getSubscribedTopic()));
        for (String topic : topics) {
            if (topicSet.contains(topic)) {
                return;
            }
            subscribe(topic, 1);
        }
    }

    @Override
    public void subscribe(String topic, int qos) {
        Set<String> topicSet = new HashSet<>(Arrays.asList(getSubscribedTopic()));
        if (topicSet.contains(topic)) {
            return;
        }
        log.debug("subscribe topic: {}", topic);
        adapter.addTopic(topic, qos);
    }

    @Override
    public void unsubscribe(String... topics) {
        log.debug("unsubscribe topic: {}", Arrays.toString(topics));
        adapter.removeTopic(topics);
    }

    public String[] getSubscribedTopic() {
        return adapter.getTopic();
    }
}
