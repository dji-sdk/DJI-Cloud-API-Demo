package com.dji.sample.component.websocket.model;

/**
 * @author sean
 * @version 0.1
 * @date 2021/11/26
 */
public enum BizCodeEnum {

    DEVICE_ONLINE("device_online"),

    DEVICE_OFFLINE("device_offline"),

    DEVICE_UPDATE_TOPO("device_update_topo"),

    DEVICE_OSD("device_osd"),

    GATEWAY_OSD("gateway_osd"),

    DOCK_OSD("dock_osd"),

    MAP_ELEMENT_CREATE("map_element_create"),

    MAP_ELEMENT_UPDATE("map_element_update"),

    MAP_ELEMENT_DELETE("map_element_delete"),

    MAP_GROUP_REFRESH("map_group_refresh"),

    FLIGHT_TASK_PROGRESS("flighttask_progress"),

    DEVICE_HMS("device_hms"),

    DEVICE_REBOOT("device_reboot"),

    DRONE_OPEN("drone_open"),

    DRONE_CLOSE("drone_close"),

    DEVICE_CHECK("device_check"),

    DRONE_FORMAT("drone_format"),

    DEVICE_FORMAT("device_format"),

    COVER_OPEN("cover_open"),

    COVER_CLOSE("cover_close"),

    PUTTER_OPEN("putter_open"),

    PUTTER_CLOSE("putter_close"),

    CHARGE_OPEN("charge_open"),

    CHARGE_CLOSE("charge_close");

    private String code;

    BizCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
