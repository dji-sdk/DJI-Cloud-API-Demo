package com.dji.sample.component.mqtt.model;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public enum EventsMethodEnum {

    FLIGHT_TASK_PROGRESS("flighttask_progress", ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS),

    FILE_UPLOAD_CALLBACK("file_upload_callback", ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK),

    HMS("hms", ChannelName.INBOUND_EVENTS_HMS),

    DEVICE_REBOOT("device_reboot", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    DRONE_OPEN("drone_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    DRONE_CLOSE("drone_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    DEVICE_CHECK("device_check", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    DRONE_FORMAT("drone_format", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    DEVICE_FORMAT("device_format", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    COVER_OPEN("cover_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    COVER_CLOSE("cover_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    PUTTER_OPEN("putter_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    PUTTER_CLOSE("putter_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    CHARGE_OPEN("charge_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    CHARGE_CLOSE("charge_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS),

    OTA_PROGRESS("ota_progress", ChannelName.INBOUND_EVENTS_OTA_PROGRESS),

    FILE_UPLOAD_PROGRESS("fileupload_progress", ChannelName.INBOUND_EVENTS_FILE_UPLOAD_PROGRESS),

    HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA("highest_priority_upload_flighttask_media", ChannelName.INBOUND_EVENTS_HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA),

    FLIGHT_TASK_READY("flighttask_ready", ChannelName.INBOUND_EVENTS_FLIGHT_TASK_READY),

    FLY_TO_POINT_PROGRESS("fly_to_point_progress", ChannelName.INBOUND_EVENTS_FLY_TO_POINT_PROGRESS),

    TAKE_OFF_TO_POINT_PROGRESS("takeoff_to_point_progress", ChannelName.INBOUND_EVENTS_TAKE_OFF_TO_POINT_PROGRESS),

    DRC_STATUS_NOTIFY("drc_status_notify", ChannelName.INBOUND_EVENTS_DRC_STATUS_NOTIFY),

    JOYSTICK_INVALID_NOTIFY("joystick_invalid_notify", ChannelName.INBOUND_EVENTS_DRC_MODE_EXIT_NOTIFY),

    UNKNOWN("Unknown", ChannelName.DEFAULT);

    private String method;

    private String channelName;

    EventsMethodEnum(String method, String channelName) {
        this.method = method;
        this.channelName = channelName;
    }

    public String getMethod() {
        return method;
    }

    public String getChannelName() {
        return channelName;
    }

    public static EventsMethodEnum find(String method) {
        return Arrays.stream(EventsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }
}
