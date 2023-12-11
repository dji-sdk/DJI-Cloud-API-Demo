package com.dji.sdk.mqtt.osd;

import com.dji.sdk.cloudapi.device.OsdDock;
import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.OsdRcDrone;
import com.dji.sdk.cloudapi.device.OsdRemoteControl;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/29
 */
public enum OsdDeviceTypeEnum {

    RC(true, OsdRemoteControl.class, ChannelName.INBOUND_OSD_RC, GatewayTypeEnum.RC),

    DOCK(true, OsdDock.class, ChannelName.INBOUND_OSD_DOCK, GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2),

    RC_DRONE(false, OsdRcDrone.class, ChannelName.INBOUND_OSD_RC_DRONE, GatewayTypeEnum.RC),

    DOCK_DRONE(false, OsdDockDrone.class, ChannelName.INBOUND_OSD_DOCK_DRONE, GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2);

    private final boolean gateway;

    private final Set<GatewayTypeEnum> gatewayType = new HashSet<>();

    private final Class classType;

    private final String channelName;

    OsdDeviceTypeEnum(boolean gateway, Class classType, String channelName, GatewayTypeEnum... gatewayType) {
        this.gateway = gateway;
        this.classType = classType;
        this.channelName = channelName;
        Collections.addAll(this.gatewayType, gatewayType);
    }

    public Set<GatewayTypeEnum> getGatewayType() {
        return gatewayType;
    }

    public boolean isGateway() {
        return gateway;
    }

    public Class getClassType() {
        return classType;
    }

    public String getChannelName() {
        return channelName;
    }

    public static OsdDeviceTypeEnum find(GatewayTypeEnum gatewayType, boolean isGateway) {
        return Arrays.stream(values()).filter(osdEnum -> osdEnum.gatewayType.contains(gatewayType) && osdEnum.gateway == isGateway).findAny()
            .orElseThrow(() -> new CloudSDKException(OsdDeviceTypeEnum.class, gatewayType, isGateway));
    }

    public static OsdDeviceTypeEnum find(Class classType) {
        return Arrays.stream(values()).filter(type -> type.classType == classType).findAny()
                .orElseThrow(() -> new CloudSDKException(OsdDeviceTypeEnum.class, classType));
    }
}
