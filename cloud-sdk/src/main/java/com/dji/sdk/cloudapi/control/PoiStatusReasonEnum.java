package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public enum PoiStatusReasonEnum {

    NORMAL(0),

    UNADAPTED_PAYLOAD(1),

    CAMERA_MODE_NOT_SUPPORTED(2),

    ILLEGAL_CMD(3),

    POSITIONING_FAILED(4),

    ON_THE_GROUND(5),

    DRONE_MODE_ERROR(6),

    NOT_AVAILABLE_MODE(7),

    RC_DISCONNECTED(8),

    ;

    private final int reason;

    PoiStatusReasonEnum(int reason) {
        this.reason = reason;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    @JsonCreator
    public static PoiStatusReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
            .orElseThrow(() -> new CloudSDKException(PoiStatusReasonEnum.class, reason));
    }

}
