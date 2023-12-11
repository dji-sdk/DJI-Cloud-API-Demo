package com.dji.sdk.cloudapi.livestream.api;

import com.dji.sdk.cloudapi.livestream.*;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.services.ServicesPublish;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.dji.sdk.mqtt.state.TopicStateRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public abstract class AbstractLivestreamService {

    @Resource
    private ServicesPublish servicesPublish;

    private static final long DEFAULT_TIMEOUT = 20_000;

    /**
     * Livestream ability update for remote control
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_LIVESTREAM_ABILITY_UPDATE)
    public void dockLivestreamAbilityUpdate(TopicStateRequest<DockLivestreamAbilityUpdate> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockLivestreamAbilityUpdate not implemented");
    }

    /**
     * Livestream ability update for dock
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_LIVESTREAM_ABILITY_UPDATE)
    public void rcLivestreamAbilityUpdate(TopicStateRequest<RcLivestreamAbilityUpdate> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcLivestreamAbilityUpdate not implemented");
    }

    /**
     * Start livestreaming
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData<String>> liveStartPush(GatewayManager gateway, LiveStartPushRequest request) {
        return servicesPublish.publish(
                new TypeReference<String>() {},
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_START_PUSH.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * Stop livestreaming
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveStopPush(GatewayManager gateway, LiveStopPushRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_STOP_PUSH.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * Set livestream quality
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveSetQuality(GatewayManager gateway, LiveSetQualityRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_SET_QUALITY.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * Set livestream lens
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveLensChange(GatewayManager gateway, LiveLensChangeRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_LENS_CHANGE.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }


}
