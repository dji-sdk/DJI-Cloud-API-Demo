package com.dji.sample.component.mqtt.config;

import com.dji.sample.component.mqtt.model.ChannelName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executor;

/**
 * Definition classes for all channels
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
public class MqttMessageChannel {

    @Autowired
    private Executor threadPool;

    @Bean(name = ChannelName.INBOUND)
    public MessageChannel inboundChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.INBOUND_STATUS)
    public MessageChannel statusChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS_ONLINE)
    public MessageChannel statusOnlineChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATUS_OFFLINE)
    public MessageChannel statusOffChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE)
    public MessageChannel stateChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE_BASIC)
    public MessageChannel stateBasicChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE_PAYLOAD)
    public MessageChannel statePayloadChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_SERVICE_REPLY)
    public MessageChannel serviceReplyChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE_CAPACITY)
    public MessageChannel stateCapacityChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE_PAYLOAD_UPDATE)
    public MessageChannel statePayloadUpdateChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_OSD)
    public MessageChannel osdChannel() {
        return new ExecutorChannel(threadPool);
    }

    @Bean(name = ChannelName.DEFAULT)
    public MessageChannel defaultChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.OUTBOUND)
    public MessageChannel outboundChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_STATE_FIRMWARE_VERSION)
    public MessageChannel stateFirmwareVersionChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS)
    public MessageChannel requestsChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS_STORAGE_CONFIG_GET)
    public MessageChannel requestsConfigGetChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS)
    public MessageChannel eventsChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS)
    public MessageChannel eventsFlightTaskProgressChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK)
    public MessageChannel eventsFileUploadCallbackChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS)
    public MessageChannel requestsAirportBindStatusChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET)
    public MessageChannel requestsAirportOrganizationGetChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND)
    public MessageChannel requestsAirportOrganizationBindChannel() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_HMS)
    public MessageChannel eventsHms() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS)
    public MessageChannel eventsControlProgress() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_OTA_PROGRESS)
    public MessageChannel eventsOtaProgress() {
        return new DirectChannel();
    }

    @Bean(name = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_PROGRESS)
    public MessageChannel eventsFileUploadProgress() {
        return new DirectChannel();
    }

}
