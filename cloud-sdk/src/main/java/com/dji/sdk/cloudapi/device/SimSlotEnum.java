package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public enum SimSlotEnum {

    UNKNOWN(0),

    SIM(1),

    ESIM(2),

    ;

    private final int slot;

    SimSlotEnum(int slot) {
        this.slot = slot;
    }

    @JsonValue
    public int getSlot() {
        return slot;
    }

    @JsonCreator
    public static SimSlotEnum find(int slot) {
        return Arrays.stream(values()).filter(slotEnum -> slotEnum.slot == slot).findAny()
            .orElseThrow(() -> new CloudSDKException(SimSlotEnum.class, slot));
    }

}
