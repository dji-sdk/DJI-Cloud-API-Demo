package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public enum ExecutionStepEnum {

    STARTING(0, 4, "MissionCenter starting, checking and resuming"),

    WAITING_STATE(5, 18, "Waiting state"),

    EXECUTING(19, 20, "Task executing"),

    RTH(21, 29, "Returning to home"),

    PULLING_LOGS(30, 39, "Pulling logs"),

    COMPLETE(40, 65534, "Interaction completed"),

    UNKNOWN(65535, 65535, "Unknown");

    private final int min;

    private final int max;

    private final String msg;

    ExecutionStepEnum(int min, int max, String msg) {
        this.min = min;
        this.max = max;
        this.msg = msg;
    }

    @JsonValue
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static ExecutionStepEnum find(int step) {
        return Arrays.stream(values()).filter(stepEnum -> stepEnum.min <= step && stepEnum.max >= step).findAny()
                .orElseThrow(() -> new CloudSDKException(ExecutionStepEnum.class, step));
    }

    @JsonCreator
    public static ExecutionStepEnum find(String msg) {
        return Arrays.stream(values()).filter(stepEnum -> stepEnum.msg.equals(msg)).findAny()
                .orElseThrow(() -> new CloudSDKException(ExecutionStepEnum.class, msg));
    }
}
