package com.dji.sdk.cloudapi.firmware.api;

import com.dji.sdk.cloudapi.firmware.FirmwareMethodEnum;
import com.dji.sdk.cloudapi.firmware.OtaCreateRequest;
import com.dji.sdk.cloudapi.firmware.OtaCreateResponse;
import com.dji.sdk.cloudapi.firmware.OtaProgress;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.events.EventsDataRequest;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import com.dji.sdk.mqtt.services.ServicesPublish;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/28
 */
public abstract class AbstractFirmwareService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * Firmware upgrade progress
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_OTA_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> otaProgress(TopicEventsRequest<EventsDataRequest<OtaProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("otaProgress not implemented");
    }

    /**
     * Firmware upgrade
     * @param gateway
     * @param request   data
     * @return  services_reply
     */
    public TopicServicesResponse<ServicesReplyData<OtaCreateResponse>> otaCreate(GatewayManager gateway, OtaCreateRequest request) {
        return servicesPublish.publish(
                new TypeReference<OtaCreateResponse>() {},
                gateway.getGatewaySn(),
                FirmwareMethodEnum.OTA_CREATE.getMethod(),
                request);
    }

}
