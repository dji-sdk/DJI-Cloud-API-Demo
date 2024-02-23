package com.dji.sdk.mqtt.events;

import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.mqtt.IMqttTopicService;
import com.dji.sdk.mqtt.TopicConst;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
public class EventsSubscribe {

    public static final String TOPIC = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "%s" + TopicConst.EVENTS_SUF;

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway, boolean unsubscribeSubDevice) {
        topicService.subscribe(String.format(TOPIC, gateway.getGatewaySn()));
        if (unsubscribeSubDevice) {
            topicService.unsubscribe(String.format(TOPIC, gateway.getDroneSn()));
            return;
        }
        if (null != gateway.getDroneSn()) {
            topicService.subscribe(String.format(TOPIC, gateway.getDroneSn()));
        }
    }

    public void unsubscribe(GatewayManager gateway) {
        topicService.unsubscribe(String.format(TOPIC, gateway.getGatewaySn()));
        if (null != gateway.getDroneSn()) {
            topicService.unsubscribe(String.format(TOPIC, gateway.getDroneSn()));
        }
    }
}
