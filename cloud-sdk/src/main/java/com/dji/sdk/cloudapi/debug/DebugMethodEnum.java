package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.common.BaseModel;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum DebugMethodEnum {

    DEBUG_MODE_OPEN("debug_mode_open", null),

    DEBUG_MODE_CLOSE("debug_mode_close", null),

    SUPPLEMENT_LIGHT_OPEN("supplement_light_open", null),

    SUPPLEMENT_LIGHT_CLOSE("supplement_light_close", null),

    DEVICE_REBOOT("device_reboot", null),

    DRONE_OPEN("drone_open", null),

    DRONE_CLOSE("drone_close", null),

    DRONE_FORMAT("drone_format", null),

    DEVICE_FORMAT("device_format", null),

    COVER_OPEN("cover_open", null),

    COVER_CLOSE("cover_close", null),

    PUTTER_OPEN("putter_open", null),

    PUTTER_CLOSE("putter_close", null),

    CHARGE_OPEN("charge_open", null),

    CHARGE_CLOSE("charge_close", null),

    BATTERY_MAINTENANCE_SWITCH("battery_maintenance_switch", BatteryMaintenanceSwitchRequest.class),

    ALARM_STATE_SWITCH("alarm_state_switch", AlarmStateSwitchRequest.class),

    BATTERY_STORE_MODE_SWITCH("battery_store_mode_switch", BatteryStoreModeSwitchRequest.class),

    SDR_WORKMODE_SWITCH("sdr_workmode_switch", SdrWorkmodeSwitchRequest.class),

    AIR_CONDITIONER_MODE_SWITCH("air_conditioner_mode_switch", AirConditionerModeSwitchRequest.class),

    ESIM_ACTIVATE("esim_activate", EsimActivateRequest.class),

    SIM_SLOT_SWITCH("sim_slot_switch", SimSlotSwitchRequest.class),

    ESIM_OPERATOR_SWITCH("esim_operator_switch", EsimOperatorSwitchRequest.class),

    ;

    private final String method;

    private final Class<? extends BaseModel> clazz;

    DebugMethodEnum(String method, Class<? extends BaseModel> clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public Class<? extends BaseModel> getClazz() {
        return clazz;
    }

    public static DebugMethodEnum find(String method) {
        return Arrays.stream(values()).filter(methodEnum -> methodEnum.method.equals(method)).findAny()
            .orElseThrow(() -> new CloudSDKException(DebugMethodEnum.class, method));
    }
}
