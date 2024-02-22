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

    RC_OSD("gateway_osd"),

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

    CHARGE_CLOSE("charge_close"),

    FILE_UPLOAD_CALLBACK("file_upload_callback"),

    FILE_UPLOAD_PROGRESS("fileupload_progress"),

    OTA_PROGRESS("ota_progress"),

    HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA("highest_priority_upload_flighttask_media"),

    CONTROL_SOURCE_CHANGE("control_source_change"),

    FLY_TO_POINT_PROGRESS("fly_to_point_progress"),

    TAKE_OFF_TO_POINT_PROGRESS("takeoff_to_point_progress"),

    DRC_STATUS_NOTIFY("drc_status_notify"),

    JOYSTICK_INVALID_NOTIFY("joystick_invalid_notify"),

    FLIGHT_AREAS_SYNC_PROGRESS("flight_areas_sync_progress"),

    FLIGHT_AREAS_DRONE_LOCATION("flight_areas_drone_location"),

    FLIGHT_AREAS_UPDATE("flight_areas_update"),

    ;

    private String code;

    BizCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
