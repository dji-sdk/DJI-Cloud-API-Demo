package com.dji.sdk.mqtt.state;

import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.cloudapi.livestream.DockLivestreamAbilityUpdate;
import com.dji.sdk.cloudapi.livestream.RcLivestreamAbilityUpdate;
import com.dji.sdk.cloudapi.property.DockDroneCommanderFlightHeight;
import com.dji.sdk.cloudapi.property.DockDroneCommanderModeLostAction;
import com.dji.sdk.cloudapi.property.DockDroneOfflineMapEnable;
import com.dji.sdk.cloudapi.property.DockDroneRthMode;
import com.dji.sdk.mqtt.ChannelName;

import java.util.Arrays;

/**
 *
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public enum StateDataKeyEnum {

    RC_AND_DRONE_FIRMWARE_VERSION(ChannelName.INBOUND_STATE_RC_AND_DRONE_FIRMWARE_VERSION, FirmwareVersion.class),

    RC_LIVE_CAPACITY(ChannelName.INBOUND_STATE_RC_LIVESTREAM_ABILITY_UPDATE, RcLivestreamAbilityUpdate.class),

    RC_DRONE_CONTROL_SOURCE(ChannelName.INBOUND_STATE_RC_CONTROL_SOURCE, RcDroneControlSource.class),

    RC_LIVE_STATUS(ChannelName.INBOUND_STATE_RC_LIVE_STATUS, RcLiveStatus.class),

    RC_PAYLOAD_FIRMWARE(ChannelName.INBOUND_STATE_RC_PAYLOAD_FIRMWARE, PayloadFirmwareVersion.class),

    DOCK_FIRMWARE_VERSION(ChannelName.INBOUND_STATE_DOCK_FIRMWARE_VERSION, DockFirmwareVersion.class),

    DOCK_LIVE_CAPACITY(ChannelName.INBOUND_STATE_DOCK_LIVESTREAM_ABILITY_UPDATE, DockLivestreamAbilityUpdate.class),

    DOCK_DRONE_CONTROL_SOURCE(ChannelName.INBOUND_STATE_DOCK_DRONE_CONTROL_SOURCE, DockDroneControlSource.class),

    DOCK_LIVE_STATUS(ChannelName.INBOUND_STATE_DOCK_LIVE_STATUS, DockLiveStatus.class),

    DOCK_DRONE_WPMZ_VERSION(ChannelName.INBOUND_STATE_DOCK_DRONE_WPMZ_VERSION, DockDroneWpmzVersion.class),

    DOCK_DRONE_THERMAL_SUPPORTED_PALETTE_STYLE(ChannelName.INBOUND_STATE_DOCK_DRONE_THERMAL_SUPPORTED_PALETTE_STYLE, DockDroneThermalSupportedPaletteStyle.class),

    DOCK_DRONE_RTH_MODE(ChannelName.INBOUND_STATE_DOCK_DRONE_RTH_MODE, DockDroneRthMode.class),

    DOCK_DRONE_CURRENT_RTH_MODE(ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_RTH_MODE, DockDroneCurrentRthMode.class),

    DOCK_DRONE_COMMANDER_MODE_LOST_ACTION(ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_MODE_LOST_ACTION, DockDroneCommanderModeLostAction.class),

    DOCK_DRONE_CURRENT_COMMANDER_FLIGHT_MODE(ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_COMMANDER_FLIGHT_MODE, DockDroneCurrentCommanderFlightMode.class),

    DOCK_DRONE_COMMANDER_FLIGHT_HEIGHT(ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_FLIGHT_HEIGHT, DockDroneCommanderFlightHeight.class),

    DOCK_DRONE_MODE_CODE_REASON(ChannelName.INBOUND_STATE_DOCK_DRONE_MODE_CODE_REASON, DockDroneModeCodeReason.class),

    DOCK_DRONE_OFFLINE_MAP_ENABLE(ChannelName.INBOUND_STATE_DOCK_DRONE_OFFLINE_MAP_ENABLE, DockDroneOfflineMapEnable.class),

    DOCK_AND_DRONE_DONGLE_INFOS(ChannelName.INBOUND_STATE_DOCK_AND_DRONE_DONGLE_INFOS, DongleInfos.class),

    DOCK_SILENT_MODE(ChannelName.INBOUND_STATE_DOCK_SILENT_MODE, DockSilentMode.class),

    UNKNOWN(ChannelName.DEFAULT, Object.class);

    private final String channelName;

    private final Class classType;

    StateDataKeyEnum(String channelName, Class classType) {
        this.channelName = channelName;
        this.classType = classType;
    }

    public Class getClassType() {
        return classType;
    }

    public String getChannelName() {
        return channelName;
    }

    public static StateDataKeyEnum find(Class clazz) {
        return Arrays.stream(values()).filter(keyEnum -> keyEnum.classType == clazz).findAny()
                .orElse(UNKNOWN);
    }

}
