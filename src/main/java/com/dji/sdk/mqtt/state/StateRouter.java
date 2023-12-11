package com.dji.sdk.mqtt.state;

import com.dji.sdk.common.Common;
import com.dji.sdk.common.SDKManager;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.dji.sdk.mqtt.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@Configuration
public class StateRouter {

    @Bean
    public IntegrationFlow stateDataRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_STATE)
                .transform(Message.class, source -> {
                    try {
                        TopicStateRequest response = Common.getObjectMapper().readValue((byte[]) source.getPayload(), new TypeReference<TopicStateRequest>() {});
                        String topic = String.valueOf(source.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                        String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(), topic.indexOf(STATE_SUF));
                        return response.setFrom(from)
                                .setData(Common.getObjectMapper().convertValue(response.getData(), getTypeReference(response.getGateway(), response.getData())));
                    } catch (IOException e) {
                        throw new CloudSDKException(e);
                    }
                }, null)
                .<TopicStateRequest, StateDataKeyEnum>route(response -> StateDataKeyEnum.find(response.getData().getClass()),
                        mapping -> Arrays.stream(StateDataKeyEnum.values()).forEach(key -> mapping.channelMapping(key, key.getChannelName())))
                .get();
    }

    private Class getTypeReference(String gatewaySn, Object data) {
        Set<String> keys = ((Map<String, Object>) data).keySet();
        switch (SDKManager.getDeviceSDK(gatewaySn).getType()) {
            case RC:
                return RcStateDataKeyEnum.find(keys).getClassType();
            case DOCK:
            case DOCK2:
                return DockStateDataKeyEnum.find(keys).getClassType();
            default:
                throw new CloudSDKException(CloudSDKErrorEnum.WRONG_DATA, "Unexpected value: " + SDKManager.getDeviceSDK(gatewaySn).getType());
        }
    }
}