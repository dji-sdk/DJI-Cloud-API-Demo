package com.dji.sdk.mqtt.osd;

import com.dji.sdk.cloudapi.device.PayloadModelConst;
import com.dji.sdk.common.Common;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.common.SDKManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.dji.sdk.mqtt.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @date 2021/11/17
 * @version 0.1
 */
@Configuration
public class OsdRouter {

    @Bean
    public IntegrationFlow osdRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_OSD)
                .transform(Message.class, source -> {
                    try {
                        TopicOsdRequest response = Common.getObjectMapper().readValue((byte[]) source.getPayload(), new TypeReference<TopicOsdRequest>() {});
                        String topic = String.valueOf(source.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                        return response.setFrom(topic.substring((THING_MODEL_PRE + PRODUCT).length(), topic.indexOf(OSD_SUF)));
                    } catch (IOException e) {
                        throw new CloudSDKException(e);
                    }
                }, null)
                .<TopicOsdRequest>handle((response, headers) -> {
                    GatewayManager gateway = SDKManager.getDeviceSDK(response.getGateway());
                    OsdDeviceTypeEnum typeEnum = OsdDeviceTypeEnum.find(gateway.getType(), response.getFrom().equals(response.getGateway()));
                    Map<String, Object> data = (Map<String, Object>) response.getData();
                    if (!typeEnum.isGateway()) {
                        List payloadData = (List) data.getOrDefault(PayloadModelConst.PAYLOAD_KEY, new ArrayList<>());
                        PayloadModelConst.getAllIndexWithPosition().stream().filter(data::containsKey)
                                .map(data::get).forEach(payloadData::add);
                        data.put(PayloadModelConst.PAYLOAD_KEY, payloadData);
                    }
                    return response.setData(Common.getObjectMapper().convertValue(data, typeEnum.getClassType()));
                })
                .<TopicOsdRequest, OsdDeviceTypeEnum>route(response -> OsdDeviceTypeEnum.find(response.getData().getClass()),
                        mapping -> Arrays.stream(OsdDeviceTypeEnum.values()).forEach(key -> mapping.channelMapping(key, key.getChannelName())))
                .get();
    }

}