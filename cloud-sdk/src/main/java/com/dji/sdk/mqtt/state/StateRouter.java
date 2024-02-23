package com.dji.sdk.mqtt.state;

import com.dji.sdk.common.Common;
import com.dji.sdk.common.SDKManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.FlowTransformWrapper;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.*;

import static com.dji.sdk.mqtt.TopicConst.*;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/17
 */
@Slf4j
@Configuration
public class StateRouter {

    @Resource
    SDKManager sdkManager;
    private MqttGatewayPublish gatewayPublish;

    @Bean
    public IntegrationFlow stateDataRouterFlow() {
        ObjectMapper objectMapper = Common.getObjectMapper();
        return IntegrationFlow
                .from(ChannelName.INBOUND_STATE)
                .transform(Message.class, source -> {
                    try {
                        TopicStateRequest response = objectMapper.readValue(
                                (byte[]) source.getPayload(),
                                new TypeReference<TopicStateRequest>() {});
                        String topic = String.valueOf(source.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                        String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(), topic.indexOf(STATE_SUF));

                        return FlowTransformWrapper.ok(response.setFrom(from));
                    } catch (Exception ex) {
                        log.warn("[StateRouter]"+ex.getMessage());
                        return FlowTransformWrapper.error();
                    }
                }, null)
                .filter(FlowTransformWrapper::continuee)
                .<FlowTransformWrapper>handle((wrapper, headers) -> {

                    TopicStateRequest response = (TopicStateRequest)wrapper.getRequest();

                    //fix: 修复设备未注册前设备推送state导致产生大量日志的问题 witcom@2023.10.08
                    try {
                        return getTypeReference(response.getGateway(), response.getData())
                                .map(clazz -> response.setData(objectMapper.convertValue(response.getData(), clazz)))
                                .orElse(null);
                    }catch (CloudSDKException ex){
                        log.warn("[StateRouter]"+ex.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .<TopicStateRequest, StateDataKeyEnum>route(response -> StateDataKeyEnum.find(response.getData().getClass()),
                        mapping -> Arrays.stream(StateDataKeyEnum.values()).forEach(key -> mapping.channelMapping(key, key.getChannelName())))
                .get();
    }


    @Bean
    public IntegrationFlow replySuccessState() {
        return IntegrationFlow
                .from(ChannelName.OUTBOUND_STATE)
                .handle(this::publish)
                .nullChannel();

    }

    private TopicStateResponse publish(TopicStateResponse request, MessageHeaders headers) {
        if (Objects.isNull(request) || Objects.isNull(request.getData())) {
            return null;
        }
        gatewayPublish.publishReply(request, headers);
        return request;
    }


    private Optional<Class> getTypeReference(String gatewaySn, Object data) {
        Set<String> keys = ((Map<String, Object>) data).keySet();
        //fix: 捕捉数据流发现在注册前设备可能会推送state主题导致产生大量日志 witcom@2023.10.08
        //GatewayTypeEnum type = sdkManager.getDeviceSDK(gatewaySn).getType();
        return sdkManager.findDeviceSDK(gatewaySn)
                .flatMap(gw -> {
                    GatewayTypeEnum type = gw.getType();
                    switch (type) {
                        case RC:
                            return RcStateDataKeyEnum.find(keys).map(v->v.getClassType());
                        case DOCK:
                        case DOCK2:
                            return DockStateDataKeyEnum.find(keys).map(v->v.getClassType()); 
                        default:
                            throw new CloudSDKException(CloudSDKErrorEnum.WRONG_DATA, "Unexpected value: " + type);
                    }
                });
    }
}