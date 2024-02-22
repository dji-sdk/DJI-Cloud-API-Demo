package com.dji.sdk.mqtt.requests;

import com.dji.sdk.common.Common;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
@Configuration
public class RequestsRouter {

    @Resource
    private MqttGatewayPublish gatewayPublish;

    @Bean
    public IntegrationFlow requestsMethodRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_REQUESTS)
                .<byte[], TopicRequestsRequest>transform(payload -> {
                    try {
                        TopicRequestsRequest response = Common.getObjectMapper().readValue(payload, TopicRequestsRequest.class);
                        return response.setData(Common.getObjectMapper().convertValue(response.getData(), RequestsMethodEnum.find(response.getMethod()).getClassType()));
                    } catch (IOException e) {
                        throw new CloudSDKException(e);
                    }
                })
                .<TopicRequestsRequest, RequestsMethodEnum>route(
                        receiver -> RequestsMethodEnum.find(receiver.getMethod()),
                                mapping -> Arrays.stream(RequestsMethodEnum.values()).forEach(
                                        methodEnum -> mapping.channelMapping(methodEnum, methodEnum.getChannelName())))
                .get();
    }

    @Bean
    public IntegrationFlow replyRequestsMethod() {
        return IntegrationFlows
                .from(ChannelName.OUTBOUND_REQUESTS)
                .handle(this::publish)
                .nullChannel();
    }

    private TopicRequestsResponse publish(TopicRequestsResponse request, MessageHeaders headers) {
        if (Objects.isNull(request)) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, "The return value cannot be null.");
        }
        gatewayPublish.publishReply(request, headers);
        return request;
    }
}
