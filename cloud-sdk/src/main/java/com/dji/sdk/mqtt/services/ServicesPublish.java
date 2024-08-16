package com.dji.sdk.mqtt.services;

import com.dji.sdk.common.Common;
import com.dji.sdk.common.PublishOption;
import com.dji.sdk.mqtt.CommonTopicResponse;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.dji.sdk.mqtt.TopicConst;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
        return this.publish(clazz, sn, method,data,ops-> ops.withBizId(bid).timeout((int)(timeout / 1000))).join();
        /*
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

         */
    }

    /**
     * Remark by witcom@2023.09.22
     * TODO: 在贡献的版本到这里就不再往下修改了,需要修改所有AbstractXXXService和AOP，改动量很大
     * 主要思想是
     * 1.提供异步支持
     * 2.Chan的实现可由用户自定义
     * 3.提供发送选项参数
     * 4.提供发送前，接收后钩子用于记录请求和返回接近最原始的记录
     */

    public <T> CompletableFuture<TopicServicesResponse<ServicesReplyData<T>>> publish(TypeReference<T> clazz, String sn, String method, Object data, Consumer<PublishOption> options){
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + Objects.requireNonNull(sn) + TopicConst.SERVICES_SUF;
        return gatewayPublish.publishWithReply(ServicesReplyReceiver.class, topic, new TopicServicesRequest<>()
                .setTimestamp(System.currentTimeMillis())
                .setMethod(method)
                .setData(Objects.requireNonNullElse(data, "")), options)
                .thenApply(response->(TopicServicesResponse)response)
                .thenApply(response->{
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
                });
    }
}
