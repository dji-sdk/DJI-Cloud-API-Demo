package com.dji.sdk.mqtt;

import com.dji.sdk.common.*;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.websocket.api.WebSocketMessageSend;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Component
public class MqttGatewayPublish {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageSend.class);

    private static final int DEFAULT_QOS = 0;
    public static final int DEFAULT_RETRY_COUNT = 2;
    public static final int DEFAULT_RETRY_TIMEOUT = 3000;

    @Resource
    private IMqttMessageGateway messageGateway;

    @Resource
    private PublishBarrier publishBarrier;

    @Resource
    private GlobalPublishOption globalOptions;

    public void publish(String topic, int qos, CommonTopicRequest request) {
        try {
            if(log.isDebugEnabled()) {
                log.debug("send topic: {}, payload: {}", topic, request.toString());
            }
            byte[] payload = Common.getObjectMapper().writeValueAsBytes(request);
            messageGateway.publish(topic, payload, qos);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish the message. {}", request.toString());
            e.printStackTrace();
        }
    }

    public void publish(String topic, int qos, CommonTopicResponse response) {
        try {
            if(log.isDebugEnabled()) {
                log.debug("send topic: {}, payload: {}", topic, response.toString());
            }
            byte[] payload = Common.getObjectMapper().writeValueAsBytes(response);
            messageGateway.publish(topic, payload, qos);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish the message. {}", response.toString());
            e.printStackTrace();
        }
    }

    public void publish(String topic, CommonTopicRequest request, int publishCount) {
        AtomicInteger time = new AtomicInteger(0);
        while (time.getAndIncrement() < publishCount) {
            this.publish(topic, DEFAULT_QOS, request);
        }
    }

    public void publish(String topic, CommonTopicRequest request) {
        this.publish(topic, DEFAULT_QOS, request);
    }

    public void publishReply(CommonTopicResponse response, MessageHeaders headers) {
        this.publish(headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF, 2, response);
    }

    public <T> CommonTopicResponse<T> publishWithReply(Class<T> clazz, String topic, CommonTopicRequest request, int retryCount, long timeout) {
        AtomicInteger time = new AtomicInteger(0);
        boolean hasBid = StringUtils.hasText(request.getBid());
        request.setBid(hasBid ? request.getBid() : UUID.randomUUID().toString());

        // Retry
        // Is Retry necessary? why not use Spring @Retryable instead?
        while (time.getAndIncrement() <= retryCount) {
            this.publish(topic, request);

            // If the message is not received in 3 seconds then resend it again.
            CommonTopicResponse<T> receiver = Chan.getInstance(request.getTid(), true).get(request.getTid(), timeout);
            // Need to match tid and bid.
            if (Objects.nonNull(receiver)
                    && receiver.getTid().equals(request.getTid())
                    && receiver.getBid().equals(request.getBid())) {
                if (clazz.isAssignableFrom(receiver.getData().getClass())) {
                    return receiver;
                }
                throw new TypeMismatchException(receiver.getData(), clazz);
            }
            // It must be guaranteed that the tid and bid of each message are different.
            if (!hasBid) {
                request.setBid(UUID.randomUUID().toString());
            }
            request.setTid(UUID.randomUUID().toString());
        }
        throw new CloudSDKException(CloudSDKErrorEnum.MQTT_PUBLISH_ABNORMAL, "No message reply received.");
    }

    public <T> CompletableFuture<CommonTopicResponse<T>> publishWithReply(Class<T> clazz, String topic, CommonTopicRequest request, Consumer<PublishOption> options){
        PublishConfiguration config = prepareConfiguration(options);
        request.setBid(config.getBid());
        request.setTid(config.getTid());

        //use to log request data or the last chance to change some data
        //CommonTopicRequest丢失了一些需要记录的内容,把这些内容封到PublishRequest交出去
        PublishRequest wrapRequest = new CommonTopicRequestWrapper<T>(clazz,topic, request, config);
        config.invokeBeforePublishHook(wrapRequest);

        //注册barrier
        String identity = publishBarrier.generateIdentity(request); //提供栅栏标识
        publishBarrier.registerRequest(identity, request);

        return CompletableFuture.supplyAsync(()->{
           this.publish(topic, request);

           if(log.isDebugEnabled()){ log.debug("等待{}指令返回",identity); };
           PublishBarrierResult result = publishBarrier.await(identity,config.getTimeout());
           config.invokeAfterPublishReplyHook(wrapRequest, result);

           if(result.isTimeout()){
               throw new CloudSDKException("Timeout"); //TODO: 换个更明确的异常更好
           }

           if(log.isDebugEnabled()){ log.debug("{}指令已返回",identity); }
           return result.getData();
        });
    }

    private PublishConfiguration prepareConfiguration(Consumer<PublishOption> options){
        PublishConfiguration config = new PublishConfiguration();
        PublishOption option = new PublishOption(config);
        options.accept(option);

        if(Strings.isNullOrEmpty(config.getBid())){
            config.setBizId(globalOptions.defaultBizId().get());
        }

        if(Strings.isNullOrEmpty(config.getTid())){
            config.setTransactionId(globalOptions.defaultTransactionId().get());
        }

        if(config.noneBeforePublishHook()){
            config.setBeforePublishHook(globalOptions.defaultBeforePublishHook());
        }

        if(config.noneAfterPublishHook()){
            config.setAfterPublishReplyHook(globalOptions.defaultAfterPublishHook());
        }
        return config;
    }

    static class CommonTopicRequestWrapper<T> implements PublishRequest{
        final CommonTopicRequest request;
        final String topic;
        final Class<T> clazz;

        final ReadonlyPublishConfiguration config;

        public  CommonTopicRequestWrapper(Class<T> clazz, String topic,CommonTopicRequest request, PublishConfiguration config) {
            this.clazz = clazz;
            this.request = request;
            this.topic = topic;
            this.config = config;
        }

        @Override
        public String getTopic() {
            return topic;
        }

        @Override
        public CommonTopicRequest getOriginRequest() {
            return request;
        }

        @Override
        public ReadonlyPublishConfiguration getConfiguration() {
            return config;
        }


    }
}