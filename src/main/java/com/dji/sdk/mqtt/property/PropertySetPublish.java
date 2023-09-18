package com.dji.sdk.mqtt.property;

import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.dji.sdk.mqtt.TopicConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
@Component
public class PropertySetPublish {

    @Resource
    private MqttGatewayPublish gatewayPublish;

    public PropertySetReplyResultEnum publish(String sn, Object data) {
        return this.publish(sn, data, MqttGatewayPublish.DEFAULT_RETRY_COUNT);
    }

    public PropertySetReplyResultEnum publish(String sn, Object data, int retryCount) {
        return this.publish(sn, data, retryCount, MqttGatewayPublish.DEFAULT_RETRY_TIMEOUT);
    }

    public PropertySetReplyResultEnum publish(String sn, Object data, int retryCount, long timeout) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + Objects.requireNonNull(sn) + TopicConst.PROPERTY_SUF + TopicConst.SET_SUF;
        return gatewayPublish.publishWithReply(
                PropertySetReplyResultEnum.class, topic, new TopicPropertySetRequest<>()
                        .setTid(UUID.randomUUID().toString())
                        .setBid(null)
                        .setTimestamp(System.currentTimeMillis())
                        .setData(Objects.requireNonNull(data)), retryCount, timeout).getData();
    }

}
