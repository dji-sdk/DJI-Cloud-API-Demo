package com.dji.sdk.mqtt.state;

import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.cloudapi.livestream.DockLivestreamAbilityUpdate;
import com.dji.sdk.cloudapi.livestream.RcLivestreamAbilityUpdate;
import com.dji.sdk.mqtt.ChannelName;

import java.util.Arrays;

/**
 *
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public enum StateDataKeyEnum {

    RC_FIRMWARE_VERSION(ChannelName.INBOUND_STATE_RC_FIRMWARE_VERSION, RcFirmwareVersion.class),

    RC_LIVE_CAPACITY(ChannelName.INBOUND_STATE_RC_LIVESTREAM_ABILITY_UPDATE, RcLivestreamAbilityUpdate.class),

    RC_CONTROL_SOURCE(ChannelName.INBOUND_STATE_RC_CONTROL_SOURCE, RcDroneControlSource.class),

    RC_LIVE_STATUS(ChannelName.INBOUND_STATE_RC_LIVE_STATUS, RcLiveStatus.class),

    RC_PAYLOAD_FIRMWARE(ChannelName.INBOUND_STATE_RC_PAYLOAD_FIRMWARE, PayloadFirmwareVersion.class),

    DOCK_FIRMWARE_VERSION(ChannelName.INBOUND_STATE_DOCK_FIRMWARE_VERSION, DockFirmwareVersion.class),

    DOCK_LIVE_CAPACITY(ChannelName.INBOUND_STATE_DOCK_LIVESTREAM_ABILITY_UPDATE, DockLivestreamAbilityUpdate.class),

    DOCK_CONTROL_SOURCE(ChannelName.INBOUND_STATE_DOCK_CONTROL_SOURCE, DockDroneControlSource.class),

    DOCK_LIVE_STATUS(ChannelName.INBOUND_STATE_DOCK_LIVE_STATUS, DockLiveStatus.class),

    WPMZ_VERSION(ChannelName.INBOUND_STATE_DOCK_WPMZ_VERSION, DockWpmzVersion.class),

    DOCK_PAYLOAD(ChannelName.INBOUND_STATE_DOCK_PAYLOAD, DockPayload.class),

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
