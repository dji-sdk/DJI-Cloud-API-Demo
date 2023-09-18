package com.dji.sample.control.model.enums;

import com.dji.sample.control.model.dto.*;
import com.dji.sample.control.service.impl.RemoteDebugHandler;
import com.dji.sdk.cloudapi.debug.DebugMethodEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum RemoteDebugMethodEnum {

    DEBUG_MODE_OPEN(DebugMethodEnum.DEBUG_MODE_OPEN, false, RemoteDebugOpenState.class),

    DEBUG_MODE_CLOSE(DebugMethodEnum.DEBUG_MODE_CLOSE, false, null),

    SUPPLEMENT_LIGHT_OPEN(DebugMethodEnum.SUPPLEMENT_LIGHT_OPEN, false, null),

    SUPPLEMENT_LIGHT_CLOSE(DebugMethodEnum.SUPPLEMENT_LIGHT_CLOSE, false, null),

    RETURN_HOME("return_home", false, ReturnHomeState.class),

    RETURN_HOME_CANCEL("return_home_cancel", false, ReturnHomeCancelState.class),

    DEVICE_REBOOT(DebugMethodEnum.DEVICE_REBOOT, true, null),

    DRONE_OPEN(DebugMethodEnum.DRONE_OPEN, true, null),

    DRONE_CLOSE(DebugMethodEnum.DRONE_CLOSE, true, null),

    DRONE_FORMAT(DebugMethodEnum.DRONE_FORMAT, true, null),

    DEVICE_FORMAT(DebugMethodEnum.DEVICE_FORMAT, true, null),

    COVER_OPEN(DebugMethodEnum.COVER_OPEN, true, null),

    COVER_CLOSE(DebugMethodEnum.COVER_CLOSE, true, null),

    PUTTER_OPEN(DebugMethodEnum.PUTTER_OPEN, true, null),

    PUTTER_CLOSE(DebugMethodEnum.PUTTER_CLOSE, true, null),

    CHARGE_OPEN(DebugMethodEnum.CHARGE_OPEN, true, null),

    CHARGE_CLOSE(DebugMethodEnum.CHARGE_CLOSE, true, null),

    BATTERY_MAINTENANCE_SWITCH(DebugMethodEnum.BATTERY_MAINTENANCE_SWITCH, false, AlarmState.class),

    ALARM_STATE_SWITCH(DebugMethodEnum.ALARM_STATE_SWITCH, false, AlarmState.class),
    
    BATTERY_STORE_MODE_SWITCH(DebugMethodEnum.BATTERY_STORE_MODE_SWITCH, false, BatteryStoreMode.class),

    SDR_WORK_MODE_SWITCH(DebugMethodEnum.SDR_WORKMODE_SWITCH, false, LinkWorkMode.class),

    AIR_CONDITIONER_MODE_SWITCH(DebugMethodEnum.AIR_CONDITIONER_MODE_SWITCH, false, AirConditionerMode.class);

    private DebugMethodEnum debugMethodEnum;

    private String method;

    private boolean progress;
    
    private Class<? extends RemoteDebugHandler> clazz;

    RemoteDebugMethodEnum(DebugMethodEnum debugMethodEnum, boolean progress, Class<? extends RemoteDebugHandler> clazz) {
        this.debugMethodEnum = debugMethodEnum;
        this.progress = progress;
        this.clazz = clazz;
        this.method = debugMethodEnum.getMethod();
    }

    RemoteDebugMethodEnum(String method, boolean progress, Class<? extends RemoteDebugHandler> clazz) {
        this.debugMethodEnum = null;
        this.progress = progress;
        this.clazz = clazz;
        this.method = method;
    }

    @JsonCreator
    public static RemoteDebugMethodEnum find(String method) {
        return Arrays.stream(values())
                .filter(methodEnum -> methodEnum.method.equals(method)
                        || (Objects.nonNull(methodEnum.debugMethodEnum)
                            && methodEnum.debugMethodEnum.getMethod().equals(method)))
                .findAny()
                .orElseThrow();
    }

}
