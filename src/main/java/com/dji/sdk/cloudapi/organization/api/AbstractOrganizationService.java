package com.dji.sdk.cloudapi.organization.api;

import com.dji.sdk.cloudapi.organization.*;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public abstract class AbstractOrganizationService {

    /**
     * Obtain organization binding information
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportBindStatusResponse>> airportBindStatus(
            TopicRequestsRequest<AirportBindStatusRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportBindStatus not implemented");
    }

    /**
     * Search for the organization information that device bound to
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportOrganizationGetResponse>> airportOrganizationGet(
            TopicRequestsRequest<AirportOrganizationGetRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportOrganizationGet not implemented");
    }

    /**
     * Device bind to organization
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportOrganizationBindResponse>> airportOrganizationBind(
            TopicRequestsRequest<AirportOrganizationBindRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportOrganizationBind not implemented");
    }
}
