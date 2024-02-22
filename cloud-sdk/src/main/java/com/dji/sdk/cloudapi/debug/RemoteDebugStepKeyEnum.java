package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public enum RemoteDebugStepKeyEnum {

    GET_BID("get_bid", "Get bid"),

    UPGRADING_PREVENT_REBOOT("upgrading_prevent_reboot", "Check if the device is being updated"),

    CHECK_WORK_MODE("check_work_mode", "Check whether to enter remote debugging mode"),

    CHECK_TASK_STATE("check_task_state", "Check if the DJI Dock is free"),

    LAND_MCU_REBOOT("land_mcu_reboot", "Land MCU reboot"),

    RAIN_MCU_REBOOT("rain_mcu_reboot", "Weather station MCU reboot"),

    CORE_MCU_REBOOT("core_mcu_reboot", "Central control MCU reboot"),

    SDR_REBOOT("sdr_reboot", "SDR reboot"),

    WRITE_REBOOT_PARAM_FILE("write_reboot_param_file", "Write reboot flag"),

    GET_DRONE_POWER_STATE("get_drone_power_state", "Get battery charge state"),

    CLOSE_PUTTER("close_putter", "Close the putter"),

    CHECK_WIRED_CONNECT_STATE("check_wired_connect_state", "Get aircraft state"),

    OPEN_DRONE("open_drone", "Open the plane"),

    OPEN_ALARM("open_alarm", "Open sound and light alarm"),

    CHECK_SCRAM_STATE("check_scram_state", "Check if the emergency stop switch is pressed"),

    OPEN_COVER("open_cover", "Open the hatch"),

    CHECK_DRONE_SDR_CONNECT_STATE("check_drone_sdr_connect_state", "Establish SDR wireless connection"),

    TURN_ON_DRONE("turn_on_drone", "Turn the plane on"),

    DRONE_PADDLE_FORWARD("drone_paddle_forward", "Turn on forward paddle"),

    CLOSE_COVER("close_cover", "Close the hatch"),

    DRONE_PADDLE_REVERSE("drone_paddle_reverse", "Turn on reverse paddle"),

    DRONE_PADDLE_STOP("drone_paddle_stop", "Stop Paddle Rotation"),

    FREE_PUTTER("free_putter", "Free Putter"),

    STOP_CHARGE("stop_charge", "Stop charging");

    private final String stepKey;

    private final String message;

    RemoteDebugStepKeyEnum(String stepKey, String message) {
        this.stepKey = stepKey;
        this.message = message;
    }

    @JsonValue
    public String getStepKey() {
        return stepKey;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator
    public static RemoteDebugStepKeyEnum find(String stepKey) {
        return Arrays.stream(values()).filter(stepKeyEnum -> stepKeyEnum.stepKey.equals(stepKey)).findAny()
            .orElseThrow(() -> new CloudSDKException(RemoteDebugStepKeyEnum.class,stepKey));
    }

}
