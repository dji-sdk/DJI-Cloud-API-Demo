package com.dji.sdk.mqtt.drc;

import com.dji.sdk.cloudapi.control.*;
import com.dji.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public enum DrcUpMethodEnum {

    DRONE_CONTROL("drone_control", ChannelName.INBOUND_DRC_UP_DRONE_CONTROL, new TypeReference<DrcUpData<DroneControlResponse>>() {}),

    DRONE_EMERGENCY_STOP("drone_emergency_stop", ChannelName.INBOUND_DRC_UP_DRONE_EMERGENCY_STOP, new TypeReference<DrcUpData>() {}),

    HEART_BEAT("heart_beat", ChannelName.INBOUND_DRC_UP_HEART_BEAT, new TypeReference<HeartBeatRequest>() {}),

    HSI_INFO_PUSH("hsi_info_push", ChannelName.INBOUND_DRC_UP_HSI_INFO_PUSH, new TypeReference<HsiInfoPush>() {}),

    DELAY_INFO_PUSH("delay_info_push", ChannelName.INBOUND_DRC_UP_DELAY_INFO_PUSH, new TypeReference<DelayInfoPush>() {}),

    OSD_INFO_PUSH("osd_info_push", ChannelName.INBOUND_DRC_UP_OSD_INFO_PUSH, new TypeReference<OsdInfoPush>() {}),

    UNKNOWN("", ChannelName.DEFAULT, new TypeReference<>() {});

    private final String method;

    private final String channelName;

    private final TypeReference classType;

    DrcUpMethodEnum(String method, String channelName, TypeReference classType) {
        this.method = method;
        this.channelName = channelName;
        this.classType = classType;
    }

    public String getMethod() {
        return method;
    }

    public String getChannelName() {
        return channelName;
    }

    public TypeReference getClassType() {
        return classType;
    }

    public static DrcUpMethodEnum find(String method) {
        return Arrays.stream(DrcUpMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny().orElse(UNKNOWN);
    }
}
