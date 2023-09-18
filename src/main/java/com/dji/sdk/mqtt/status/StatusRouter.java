package com.dji.sdk.mqtt.status;

import com.dji.sdk.cloudapi.device.UpdateTopo;
import com.dji.sdk.common.Common;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.dji.sdk.mqtt.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/12
 * @version 0.1
 */
@Configuration
public class StatusRouter {

    @Resource
    private MqttGatewayPublish gatewayPublish;

    @Bean
    public IntegrationFlow statusRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_STATUS)
                .transform(Message.class, source -> {
                    try {
                        TopicStatusRequest<UpdateTopo> response = Common.getObjectMapper().readValue((byte[]) source.getPayload(), new TypeReference<TopicStatusRequest<UpdateTopo>>() {});
                        String topic = String.valueOf(source.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                        return response.setFrom(topic.substring((BASIC_PRE + PRODUCT).length(), topic.indexOf(STATUS_SUF)));
                    } catch (IOException e) {
                        throw new CloudSDKException(e);
                    }
                }, null)
                .<TopicStatusRequest<UpdateTopo>, Boolean>route(
                        response -> Optional.ofNullable(response.getData()).map(UpdateTopo::getSubDevices).map(CollectionUtils::isEmpty).orElse(true),
                        mapping -> mapping.channelMapping(true, ChannelName.INBOUND_STATUS_OFFLINE)
                                .channelMapping(false, ChannelName.INBOUND_STATUS_ONLINE))
                .get();
    }

    @Bean
    public IntegrationFlow replySuccessStatus() {
        return IntegrationFlows
                .from(ChannelName.OUTBOUND_STATUS)
                .handle(this::publish)
                .nullChannel();

    }

    private TopicStatusResponse publish(TopicStatusResponse request, MessageHeaders headers) {
        if (Objects.isNull(request)) {
            return null;
        }
        gatewayPublish.publishReply(request, headers);
        return request;
    }
}