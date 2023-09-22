package com.dji.sdk.mqtt.osd;

import com.dji.sdk.cloudapi.device.PayloadModelEnum;
import com.dji.sdk.common.Common;
import com.dji.sdk.common.GatewayManager;
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
import java.util.*;

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

                    // fix: getDeviceSDK抛出异常导致在设备未注册的情况下报osd时产生大量日志 witcom@2023.09.22
                    //GatewayManager gateway = SDKManager.getDeviceSDK(response.getGateway());
                    return SDKManager.findDeviceSDK(response.getGateway())
                            .map(gateway-> {

                                OsdDeviceTypeEnum typeEnum = OsdDeviceTypeEnum.find(gateway.getType(), response.getFrom().equals(response.getGateway()));
                                Map<String, Object> data = (Map<String, Object>) response.getData();
                                if (!typeEnum.isGateway()) {
                                    List payloadData = (List) data.getOrDefault(PayloadModelEnum.PAYLOAD_KEY, new ArrayList<>());
                                    PayloadModelEnum.getAllIndexWithPosition().stream().filter(data::containsKey)
                                            .map(data::get).forEach(payloadData::add);
                                    data.put(PayloadModelEnum.PAYLOAD_KEY, payloadData);
                                }
                                return response.setData(Common.getObjectMapper().convertValue(data, typeEnum.getClassType()));
                            })
                            .orElse(null);
                })
                .<TopicOsdRequest>filter(Objects::nonNull)
                .<TopicOsdRequest, OsdDeviceTypeEnum>route(response -> OsdDeviceTypeEnum.find(response.getData().getClass()),
                        mapping -> Arrays.stream(OsdDeviceTypeEnum.values())
                                .forEach(key -> mapping.channelMapping(key, key.getChannelName())))
                .get();
    }

}