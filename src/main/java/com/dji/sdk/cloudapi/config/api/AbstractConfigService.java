package com.dji.sdk.cloudapi.config.api;

import com.dji.sdk.cloudapi.config.ProductConfigResponse;
import com.dji.sdk.cloudapi.config.RequestsConfigRequest;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public abstract class AbstractConfigService {

    /**
     * Inform of file uploading progress
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return requests_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_CONFIG, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<ProductConfigResponse> requestsConfig(TopicRequestsRequest<RequestsConfigRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("requestsConfig not implemented");
    }

}
