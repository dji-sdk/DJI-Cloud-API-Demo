package com.dji.sdk.cloudapi.map.api;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.map.MapMethodEnum;
import com.dji.sdk.cloudapi.map.OfflineMapGetRequest;
import com.dji.sdk.cloudapi.map.OfflineMapGetResponse;
import com.dji.sdk.cloudapi.map.OfflineMapSyncProgress;
import com.dji.sdk.cloudapi.property.DockDroneOfflineMapEnable;
import com.dji.sdk.config.version.CloudSDKVersionEnum;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import com.dji.sdk.mqtt.services.ServicesPublish;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.dji.sdk.mqtt.state.TopicStateRequest;
import com.dji.sdk.mqtt.state.TopicStateResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/19
 */
public abstract class AbstractOfflineMapService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * When the offline map is closed, offline map synchronization will no longer automatically synchronize.
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_OFFLINE_MAP_ENABLE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneOfflineMapEnable(TopicStateRequest<DockDroneOfflineMapEnable> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneOfflineMapEnable not implemented");
    }

    /**
     * Actively trigger offline map updates.
     * After receiving the message, the airport will actively pull offline map information at the appropriate time and trigger the offline map synchronization mechanism.
     * @param gateway   gateway device
     * @return  services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicServicesResponse<ServicesReplyData> offlineMapUpdate(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                MapMethodEnum.OFFLINE_MAP_UPDATE.getMethod());
    }

    /**
     * Offline map file synchronization status
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_OFFLINE_MAP_SYNC_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicRequestsResponse<MqttReply> offlineMapSyncProgress(TopicRequestsRequest<OfflineMapSyncProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("offlineMapSyncProgress not implemented");
    }

    /**
     * The dock will actively pull the latest offline map file information.
     * From this information, it will check whether the aircraft's offline map file name or checksum has changed.
     * Once a change is found, offline map synchronization will be triggered.
     * Otherwise, synchronization will not be triggered.
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_OFFLINE_MAP_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicRequestsResponse<MqttReply<OfflineMapGetResponse>> offlineMapGet(TopicRequestsRequest<OfflineMapGetRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("offlineMapGet not implemented");
    }
}
