package com.dji.sdk.mqtt.services;

import com.dji.sdk.common.GatewayManager;
import com.dji.sdk.mqtt.IMqttTopicService;
import com.dji.sdk.mqtt.TopicConst;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
@Component
public class ServicesSubscribe {

    public static final String TOPIC = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "%s" + TopicConst.SERVICES_SUF + TopicConst._REPLY_SUF;

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway) {
        topicService.subscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }

    public void unsubscribe(GatewayManager gateway) {
        topicService.unsubscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }
}
