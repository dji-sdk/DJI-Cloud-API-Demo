package com.dji.sdk.cloudapi.device.api;

import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.osd.TopicOsdRequest;
import com.dji.sdk.mqtt.state.TopicStateRequest;
import com.dji.sdk.mqtt.status.TopicStatusRequest;
import com.dji.sdk.mqtt.status.TopicStatusResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AbstractDeviceService {

    /**
     * osd dock
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_DOCK)
    public void osdDock(TopicOsdRequest<OsdDock> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdDock not implemented");
    }

    /**
     * osd dock drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_DOCK_DRONE)
    public void osdDockDrone(TopicOsdRequest<OsdDockDrone> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdDockDrone not implemented");
    }

    /**
     * osd remote control
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_RC)
    public void osdRemoteControl(TopicOsdRequest<OsdRemoteControl> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdRemoteControl not implemented");
    }

    /**
     * osd remote control drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_RC_DRONE)
    public void osdRcDrone(TopicOsdRequest<OsdRcDrone> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdRcDrone not implemented");
    }

    /**
     * Gateway device + sub device online
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_ONLINE, outputChannel = ChannelName.OUTBOUND_STATUS)
    public TopicStatusResponse<MqttReply> updateTopoOnline(TopicStatusRequest<UpdateTopo> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("updateTopoOnline not implemented");
    }

    /**
     * Sub device offline
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_OFFLINE, outputChannel = ChannelName.OUTBOUND_STATUS)
    public TopicStatusResponse<MqttReply> updateTopoOffline(TopicStatusRequest<UpdateTopo> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("updateTopoOffline not implemented");
    }

    /**
     * Firmware version update for dock and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_FIRMWARE_VERSION)
    public void dockFirmwareVersionUpdate(TopicStateRequest<DockFirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockFirmwareVersionUpdate not implemented");
    }

    /**
     * Firmware version update for remote control and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_FIRMWARE_VERSION)
    public void rcFirmwareVersionUpdate(TopicStateRequest<RcFirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcFirmwareVersionUpdate not implemented");
    }

    /**
     * Drone control source update for dock and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_CONTROL_SOURCE)
    public void dockControlSourceUpdate(TopicStateRequest<DockDroneControlSource> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockControlSourceUpdate not implemented");
    }

    /**
     * Drone control source update for remote control and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_CONTROL_SOURCE)
    public void rcControlSourceUpdate(TopicStateRequest<RcDroneControlSource> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcControlSourceUpdate not implemented");
    }

    /**
     * Live status update for dock and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_LIVE_STATUS)
    public void dockLiveStatusUpdate(TopicStateRequest<DockLiveStatus> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockLiveStatusUpdate not implemented");
    }

    /**
     * Live status source update for remote control and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_LIVE_STATUS)
    public void rcLiveStatusUpdate(TopicStateRequest<RcLiveStatus> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcLiveStatusUpdate not implemented");
    }

    /**
     * Payload firmware version update for remote control and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_PAYLOAD_FIRMWARE)
    public void rcPayloadFirmwareVersionUpdate(TopicStateRequest<PayloadFirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcPayloadFirmwareVersionUpdate not implemented");
    }

    /**
     * Wpmz firmware version update for drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_WPMZ_VERSION)
    public void dockWpmzVersionUpdate(TopicStateRequest<DockWpmzVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockWpmzVersionUpdate not implemented");
    }

    /**
     * Styles supported by the IR palette
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_PAYLOAD)
    public void dockPayload(TopicStateRequest<DockPayload> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockPayload not implemented");
    }

}
