package com.dji.sdk.cloudapi.media.api;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.media.*;
import com.dji.sdk.cloudapi.storage.StsCredentialsResponse;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import com.dji.sdk.mqtt.services.ServicesPublish;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public abstract class AbstractMediaService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * Result reporting of media file uploading
     * @param request
     * @param headers
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> fileUploadCallback(TopicEventsRequest<FileUploadCallback> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("fileUploadCallback not implemented");
    }

    /**
     * Priority report of the media file uploading
     * @param request
     * @param headers
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> highestPriorityUploadFlightTaskMedia(TopicEventsRequest<HighestPriorityUploadFlightTaskMedia> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("highestPriorityUploadFlightTaskMedia not implemented");
    }

    /**
     * Set the uploading file to highest priority
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> uploadFlighttaskMediaPrioritize(GatewayManager gateway, UploadFlighttaskMediaPrioritize request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                MediaMethodEnum.UPLOAD_FLIGHTTASK_MEDIA_PRIORITIZE.getMethod(),
                request);
    }

    /**
     * Obtain upload temporary credentials
     * @param request
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<StsCredentialsResponse>> storageConfigGet(TopicRequestsRequest<StorageConfigGet> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("storageConfigGet not implemented");
    }

}
