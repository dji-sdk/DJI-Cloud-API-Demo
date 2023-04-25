package com.dji.sample.control.model.enums;

import com.dji.sample.control.model.dto.*;
import com.dji.sample.control.service.impl.RemoteDebugHandler;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum RemoteDebugMethodEnum {

    DEBUG_MODE_OPEN("debug_mode_open", false, RemoteDebugOpenState.class),

    DEBUG_MODE_CLOSE("debug_mode_close", false, null),

    SUPPLEMENT_LIGHT_OPEN("supplement_light_open", false, null),

    SUPPLEMENT_LIGHT_CLOSE("supplement_light_close", false, null),

    RETURN_HOME("return_home", false, ReturnHomeState.class),

    DEVICE_REBOOT("device_reboot", true, null),

    DRONE_OPEN("drone_open", true, null),

    DRONE_CLOSE("drone_close", true, null),

    DEVICE_CHECK("device_check", true, null),

    DRONE_FORMAT("drone_format", true, null),

    DEVICE_FORMAT("device_format", true, null),

    COVER_OPEN("cover_open", true, null),

    COVER_CLOSE("cover_close", true, null),

    PUTTER_OPEN("putter_open", true, null),

    PUTTER_CLOSE("putter_close", true, null),

    CHARGE_OPEN("charge_open", true, null),

    CHARGE_CLOSE("charge_close", true, null),

    BATTERY_MAINTENANCE_SWITCH("battery_maintenance_switch", true, AlarmState.class),
    
    ALARM_STATE_SWITCH("alarm_state_switch", true, AlarmState.class),
    
    BATTERY_STORE_MODE_SWITCH("battery_store_mode_switch", true, BatteryStoreMode.class),

    SDR_WORK_MODE_SWITCH("sdr_workmode_switch", false, LinkWorkMode.class),
    
    UNKNOWN("unknown", false, null);

    private String method;

    private Boolean progress;
    
    private Class<? extends RemoteDebugHandler> clazz;

    RemoteDebugMethodEnum(String method, Boolean progress, Class<? extends RemoteDebugHandler> clazz) {
        this.method = method;
        this.progress = progress;
        this.clazz = clazz;
    }

    public static RemoteDebugMethodEnum find(String method) {
        return Arrays.stream(RemoteDebugMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }

}
