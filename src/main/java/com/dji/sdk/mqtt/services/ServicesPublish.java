package com.dji.sdk.mqtt.services;

import com.dji.sdk.common.Common;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.dji.sdk.mqtt.TopicConst;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ServicesPublish {

    @Resource
    private MqttGatewayPublish gatewayPublish;

    public <T> TopicServicesResponse<ServicesReplyData<T>>  publish(TypeReference<T> clazz, String sn, String method) {
        return this.publish(clazz, sn, method, null);
    }

    public <T> TopicServicesResponse<ServicesReplyData<T>> publish(TypeReference<T> clazz, String sn, String method, Object data) {
        return this.publish(clazz, sn, method, data, MqttGatewayPublish.DEFAULT_RETRY_COUNT);
    }

    public <T> TopicServicesResponse<ServicesReplyData<T>>  publish(TypeReference<T> clazz, String sn, String method, Object data, int retryCount) {
        return this.publish(clazz, sn, method, data, retryCount, MqttGatewayPublish.DEFAULT_RETRY_TIMEOUT);
    }

    public <T> TopicServicesResponse<ServicesReplyData<T>>  publish(TypeReference<T> clazz, String sn, String method, Object data, long timeout) {
        return this.publish(clazz, sn, method, data, MqttGatewayPublish.DEFAULT_RETRY_COUNT, timeout);
    }

    public <T> TopicServicesResponse<ServicesReplyData<T>>  publish(TypeReference<T> clazz, String sn, String method, Object data, int retryCount, long timeout) {
        return this.publish(clazz, sn, method, data, null, retryCount, timeout);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method) {
        return this.publish(sn, method, null, null);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data) {
        return this.publish(sn, method, data, null);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, int retryCount) {
        return this.publish(sn, method, data, null, retryCount);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, long timeout) {
        return this.publish(sn, method, data, null, timeout);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, int retryCount, long timeout) {
        return this.publish(sn, method, data, null, retryCount, timeout);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, String bid) {
        return this.publish(sn, method, data, bid, MqttGatewayPublish.DEFAULT_RETRY_COUNT);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, String bid, int retryCount) {
        return this.publish(sn, method, data, bid, retryCount, MqttGatewayPublish.DEFAULT_RETRY_TIMEOUT);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, String bid, long timeout) {
        return this.publish(sn, method, data, bid, MqttGatewayPublish.DEFAULT_RETRY_COUNT, timeout);
    }

    public TopicServicesResponse<ServicesReplyData> publish(String sn, String method, Object data, String bid, int retryCount, long timeout) {
        return (TopicServicesResponse) this.publish(null, sn, method, data, bid, retryCount, timeout);
    }

    public <T> TopicServicesResponse<ServicesReplyData<T>> publish(
            TypeReference<T> clazz, String sn, String method, Object data, String bid, int retryCount, long timeout) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + Objects.requireNonNull(sn) + TopicConst.SERVICES_SUF;
        TopicServicesResponse response = (TopicServicesResponse) gatewayPublish.publishWithReply(
                ServicesReplyReceiver.class, topic, new TopicServicesRequest<>()
                        .setTid(UUID.randomUUID().toString())
                        .setBid(bid)
                        .setTimestamp(System.currentTimeMillis())
                        .setMethod(method)
                        .setData(Objects.requireNonNullElse(data, "")), retryCount, timeout);
        ServicesReplyReceiver replyReceiver = (ServicesReplyReceiver) response.getData();
        ServicesReplyData<T> reply = new ServicesReplyData<T>().setResult(replyReceiver.getResult());
        if (Objects.isNull(clazz)) {
            reply.setOutput((T) Objects.requireNonNullElse(
                    replyReceiver.getOutput(), Objects.requireNonNullElse(replyReceiver.getInfo(), "")));
            return response.setData(reply);
        }
        // put together in "output"
        ObjectMapper mapper = Common.getObjectMapper();
        if (Objects.nonNull(replyReceiver.getInfo())) {
            reply.setOutput(mapper.convertValue(replyReceiver.getInfo(), clazz));
        }
        if (Objects.nonNull(replyReceiver.getOutput())) {
            reply.setOutput(mapper.convertValue(replyReceiver.getOutput(), clazz));
        }
        return response.setData(reply);
    }

}
