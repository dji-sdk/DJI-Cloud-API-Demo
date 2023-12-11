package com.dji.sdk.mqtt.state;

import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.cloudapi.livestream.DockLivestreamAbilityUpdate;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public enum DockStateDataKeyEnum {

    FIRMWARE_VERSION(Set.of("firmware_version"), DockFirmwareVersion.class),

    LIVE_CAPACITY(Set.of("live_capacity"), DockLivestreamAbilityUpdate.class),

    CONTROL_SOURCE(Set.of("control_source"), DockDroneControlSource.class),

    LIVE_STATUS(Set.of("live_status"), DockLiveStatus.class),

    WPMZ_VERSION(Set.of("wpmz_version"), DockWpmzVersion.class),

    PAYLOAD(PayloadModelConst.getAllIndexWithPosition(), DockPayload.class)
    ;

    private final Set<String> keys;

    private final Class classType;


    DockStateDataKeyEnum(Set<String> keys, Class classType) {
        this.keys = keys;
        this.classType = classType;
    }

    public Class getClassType() {
        return classType;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public static DockStateDataKeyEnum find(Set<String> keys) {
        return Arrays.stream(values()).filter(keyEnum -> !Collections.disjoint(keys, keyEnum.keys)).findAny()
                .orElseThrow(() -> new CloudSDKException(DockStateDataKeyEnum.class, keys));
    }

}
