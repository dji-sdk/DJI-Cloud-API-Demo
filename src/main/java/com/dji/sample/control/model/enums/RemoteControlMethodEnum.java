package com.dji.sample.control.model.enums;

import com.dji.sample.control.model.dto.BatteryStoreMode;
import com.dji.sample.manage.model.enums.StateSwitchReceiver;
import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum RemoteControlMethodEnum {

    DEBUG_MODE_OPEN("debug_mode_open", false, null),

    DEBUG_MODE_CLOSE("debug_mode_close", false, null),

    SUPPLEMENT_LIGHT_OPEN("supplement_light_open", false, null),

    SUPPLEMENT_LIGHT_CLOSE("supplement_light_close", false, null),

    RETURN_HOME("return_home", false, null),

    SDR_WORKMODE_SWITCH("sdr_workmode_switch", false, null),

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

    BATTERY_MAINTENANCE_SWITCH("battery_maintenance_switch", true, StateSwitchReceiver.class),
    
    ALARM_STATE_SWITCH("alarm_state_switch", true, StateSwitchReceiver.class),
    
    BATTERY_STORE_MODE_SWITCH("battery_store_mode_switch", true, BatteryStoreMode.class),
    
    UNKNOWN("unknown", false, null);

    private String method;

    private Boolean progress;
    
    private Class<? extends BasicDeviceProperty> clazz;

    RemoteControlMethodEnum(String method, Boolean progress, Class<? extends BasicDeviceProperty> clazz) {
        this.method = method;
        this.progress = progress;
        this.clazz = clazz;
    }

    public static RemoteControlMethodEnum find(String method) {
        return Arrays.stream(RemoteControlMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }

}
