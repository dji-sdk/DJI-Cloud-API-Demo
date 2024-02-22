package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public enum CameraIsoEnum {

    AUTO(0),

    AUTO_HIGH_SENSE(1),

    _50(2),

    _100(3),

    _200(4),

    _400(5),

    _800(6),

    _1600(7),

    _3200(8),

    _6400(9),

    _12800(10),

    _25600(11),

    FIXED(255),

    ;

    private final int iso;

    CameraIsoEnum(int iso) {
        this.iso = iso;
    }

    @JsonValue
    public int getIso() {
        return iso;
    }

    @JsonCreator
    public static CameraIsoEnum find(int iso) {
        return Arrays.stream(values()).filter(isoEnum -> isoEnum.iso == iso).findAny()
            .orElseThrow(() -> new CloudSDKException(CameraIsoEnum.class, iso));
    }

}
