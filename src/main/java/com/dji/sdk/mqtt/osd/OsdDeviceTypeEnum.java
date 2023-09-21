package com.dji.sdk.mqtt.osd;

import com.dji.sdk.cloudapi.device.OsdDock;
import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.OsdRcDrone;
import com.dji.sdk.cloudapi.device.OsdRemoteControl;
import com.dji.sdk.common.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.ChannelName;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/29
 */
public enum OsdDeviceTypeEnum {

    RC(true, GatewayTypeEnum.RC, OsdRemoteControl.class, ChannelName.INBOUND_OSD_RC),

    DOCK(true, GatewayTypeEnum.DOCK, OsdDock.class, ChannelName.INBOUND_OSD_DOCK),

    RC_DRONE(false, GatewayTypeEnum.RC, OsdRcDrone.class, ChannelName.INBOUND_OSD_RC_DRONE),

    DOCK_DRONE(false, GatewayTypeEnum.DOCK, OsdDockDrone.class, ChannelName.INBOUND_OSD_DOCK_DRONE);

    private final boolean gateway;

    private final GatewayTypeEnum gatewayType;

    private final Class classType;

    private final String channelName;

    OsdDeviceTypeEnum(boolean gateway, GatewayTypeEnum gatewayType, Class classType, String channelName) {
        this.gateway = gateway;
        this.gatewayType = gatewayType;
        this.classType = classType;
        this.channelName = channelName;
    }

    public GatewayTypeEnum getGatewayType() {
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
        return Arrays.stream(values()).filter(osdEnum -> osdEnum.gatewayType == gatewayType && osdEnum.gateway == isGateway).findAny()
            .orElseThrow(() -> new CloudSDKException(OsdDeviceTypeEnum.class, gatewayType, isGateway));
    }

    public static OsdDeviceTypeEnum find(Class classType) {
        return Arrays.stream(values()).filter(type -> type.classType == classType).findAny()
                .orElseThrow(() -> new CloudSDKException(OsdDeviceTypeEnum.class, classType));
    }
}
