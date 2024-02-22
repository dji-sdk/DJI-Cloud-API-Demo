package com.dji.sdk.mqtt.drc;

import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.dji.sdk.mqtt.TopicConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
@Component
public class DrcDownPublish {

    @Resource
    private MqttGatewayPublish gatewayPublish;

    public static final int DEFAULT_PUBLISH_COUNT = 5;

    public void publish(String sn, String method) {
        this.publish(sn, method, null);
    }

    public void publish(String sn, String method, Object data) {
        this.publish(sn, method, data, DEFAULT_PUBLISH_COUNT);
    }

    public void publish(String sn, String method, Object data, int publishCount) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + Objects.requireNonNull(sn) + TopicConst.DRC + TopicConst.DOWN;
        gatewayPublish.publish(topic,
                new TopicDrcRequest<>()
                        .setMethod(method)
                        .setData(Objects.requireNonNullElse(data, "")),
                publishCount);
    }

}
