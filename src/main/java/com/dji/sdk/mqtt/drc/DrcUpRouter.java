package com.dji.sdk.mqtt.drc;

import com.dji.sdk.common.Common;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Configuration
public class DrcUpRouter {

    @Bean
    public IntegrationFlow drcUpRouterFlow() {
        return IntegrationFlows
                .from(ChannelName.INBOUND_DRC_UP)
                .transform(Message.class, source -> {
                    try {
                        TopicDrcRequest data = Common.getObjectMapper().readValue((byte[]) source.getPayload(), TopicDrcRequest.class);
                        return data.setData(Common.getObjectMapper().convertValue(data.getData(), DrcUpMethodEnum.find(data.getMethod()).getClassType()));
                    } catch (IOException e) {
                        throw new CloudSDKException(e);
                    }
                }, null)
                .<TopicDrcRequest, DrcUpMethodEnum>route(
                        response -> DrcUpMethodEnum.find(response.getMethod()),
                        mapping -> Arrays.stream(DrcUpMethodEnum.values()).forEach(
                                methodEnum -> mapping.channelMapping(methodEnum, methodEnum.getChannelName())))
                .get();
    }
}
