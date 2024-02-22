package com.dji.sample.manage.model.enums;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/15
 */
public enum ExitWaylineWhenRcLostActionEnum {

    CONTINUE_WAYLINE, EXECUTE_RC_LOST_ACTION;

    @JsonValue
    public int getVal() {
        return ordinal();
    }

    @JsonCreator
    public static ExitWaylineWhenRcLostActionEnum find(int val) {
        return Arrays.stream(values()).filter(lostActionEnum -> lostActionEnum.ordinal() == val).findAny()
                .orElseThrow(() -> new CloudSDKException(ExitWaylineWhenRcLostActionEnum.class, val));
    }
}
