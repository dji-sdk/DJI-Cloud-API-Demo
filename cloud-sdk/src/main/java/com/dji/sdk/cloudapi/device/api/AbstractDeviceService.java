package com.dji.sdk.cloudapi.device.api;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.cloudapi.property.DockDroneCommanderFlightHeight;
import com.dji.sdk.cloudapi.property.DockDroneCommanderModeLostAction;
import com.dji.sdk.cloudapi.property.DockDroneRthMode;
import com.dji.sdk.config.version.CloudSDKVersionEnum;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.mqtt.ChannelName;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.osd.TopicOsdRequest;
import com.dji.sdk.mqtt.state.TopicStateRequest;
import com.dji.sdk.mqtt.state.TopicStateResponse;
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
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_AND_DRONE_FIRMWARE_VERSION)
    public void rcAndDroneFirmwareVersionUpdate(TopicStateRequest<FirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcAndDroneFirmwareVersionUpdate not implemented");
    }

    /**
     * Drone control source update for dock and drone
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CONTROL_SOURCE)
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
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_WPMZ_VERSION)
    public void dockWpmzVersionUpdate(TopicStateRequest<DockDroneWpmzVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockWpmzVersionUpdate not implemented");
    }

    /**
     * Styles supported by the IR palette
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_THERMAL_SUPPORTED_PALETTE_STYLE)
    public void dockThermalSupportedPaletteStyle(TopicStateRequest<DockDroneThermalSupportedPaletteStyle> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockThermalSupportedPaletteStyle not implemented");
    }

    /**
     * Under optimal RTH mode, aircraft will automatically plan the optimal return altitude.
     * When the environment and lighting do not meet the requirements of the visual system (such as direct sunlight in the evening or no light at night), the aircraft will perform a straight-line return at the altitude you have set.
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_RTH_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneRthMode(TopicStateRequest<DockDroneRthMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockRthMode not implemented");
    }

    /**
     * Current RTH height mode
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_RTH_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCurrentRthMode(TopicStateRequest<DockDroneCurrentRthMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockCurrentRthMode not implemented");
    }

    /**
     * To-point flight mission out of control action
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_MODE_LOST_ACTION, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCommanderModeLostAction(TopicStateRequest<DockDroneCommanderModeLostAction> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCommanderModeLostAction not implemented");
    }

    /**
     * Current mode of to-point flight mission
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_COMMANDER_FLIGHT_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCurrentCommanderFlightMode(TopicStateRequest<DockDroneCurrentCommanderFlightMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCurrentCommanderFlightMode not implemented");
    }

    /**
     * Relative to (airport) takeoff point altitude.
     * ALT.
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_FLIGHT_HEIGHT, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCommanderFlightHeight(TopicStateRequest<DockDroneCommanderFlightHeight> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCommanderFlightHeight not implemented");
    }

    /**
     * The reason why the drone enters current state
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_MODE_CODE_REASON, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneModeCodeReason(TopicStateRequest<DockDroneModeCodeReason> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneModeCodeReason not implemented");
    }

    /**
     * 4g dongle information
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_AND_DRONE_DONGLE_INFOS, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dongleInfos(TopicStateRequest<DongleInfos> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dongleInfos not implemented");
    }

    /**
     * silent mode
     * @param request  data
     * @param headers  The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_SILENT_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockSilentMode(TopicStateRequest<DockSilentMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockSilentMode not implemented");
    }

}
