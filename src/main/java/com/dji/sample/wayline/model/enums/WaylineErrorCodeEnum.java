package com.dji.sample.wayline.model.enums;

import com.dji.sdk.common.IErrorInfo;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/17
 */
public enum WaylineErrorCodeEnum implements IErrorInfo {

    SUCCESS(0, "success", false),

    EMERGENCY_BUTTON(316026, "The emergency button at the dock was pressed.", true),
    
    NOT_IDLE(319001, "Task Center is not currently idle.", true),
    
    PERFORMING_TASK(319016, "The dock is performing other tasks.", true),
    
    EXPORTING_LOGS(319018, "The dock is exporting logs.", true),
    
    PULLING_LOGS(319019, "The dock is pulling logs.", true),
    
    HEIGHT_LIMIT(321513, "The wayline altitude has exceeded the height limit of the drone.", true),
    
    DISTANCE_LIMIT(321514, "The wayline distance has exceeded the limit of the drone.", true),

    RESTRICTED_FLIGHT_AREA(321515, "The wayline passes through a restricted flight area.", true),
    
    SDR_DISCONNECT(514120, "The sdr link between the dock and the drone is disconnected.", true),

    HEAVY_RAIN(514134, "Heavy rain prevented the flight.", true),
    
    STRONG_WIND(514135, "Strong wind prevented the flight.", true),
    
    POWER_DISCONNECT(514136, "The dock's power supply is disconnected.", true),

    LOW_TEMPERATURE(514137, "The low temperature of the environment prevented flight.", true),

    DEBUGGING(514145, "The dock is being debugged.", true),

    REMOTE_DEBUGGING(514146, "The dock is being debugged remotely.", true),

    DOCK_UPGRADING(514147, "The dock is being upgraded.", true),

    DOCK_WORKING(514148, "The dock is working and cannot perform new tasks.", true),

    UNKNOWN(-1, "Unknown wayline error.", false);
    
    private String msg;

    private int code;

    boolean block; 
    
    WaylineErrorCodeEnum(int code, String msg, boolean block) {
        this.code = code;
        this.msg = msg;
        this.block = block;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public boolean isBlock() {
        return block;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static WaylineErrorCodeEnum find(int code) {
        return Arrays.stream(WaylineErrorCodeEnum.values()).filter(error -> error.code == code).findAny().orElse(UNKNOWN);
    }
}
