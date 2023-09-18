package com.dji.sdk.mqtt.events;

import com.dji.sdk.cloudapi.control.DrcStatusNotify;
import com.dji.sdk.cloudapi.control.FlyToPointProgress;
import com.dji.sdk.cloudapi.control.JoystickInvalidNotify;
import com.dji.sdk.cloudapi.control.TakeoffToPointProgress;
import com.dji.sdk.cloudapi.debug.RemoteDebugProgress;
import com.dji.sdk.cloudapi.firmware.OtaProgress;
import com.dji.sdk.cloudapi.hms.Hms;
import com.dji.sdk.cloudapi.log.FileUploadProgress;
import com.dji.sdk.cloudapi.media.FileUploadCallback;
import com.dji.sdk.cloudapi.media.HighestPriorityUploadFlightTaskMedia;
import com.dji.sdk.cloudapi.wayline.DeviceExitHomingNotify;
import com.dji.sdk.cloudapi.wayline.FlighttaskProgress;
import com.dji.sdk.cloudapi.wayline.FlighttaskReady;
import com.dji.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public enum EventsMethodEnum {

    FLIGHTTASK_PROGRESS("flighttask_progress", ChannelName.INBOUND_EVENTS_FLIGHTTASK_PROGRESS, new TypeReference<EventsDataRequest<FlighttaskProgress>>() {}),

    DEVICE_EXIT_HOMING_NOTIFY("device_exit_homing_notify", ChannelName.INBOUND_EVENTS_DEVICE_EXIT_HOMING_NOTIFY, new TypeReference<DeviceExitHomingNotify>() {}),

    FILE_UPLOAD_CALLBACK("file_upload_callback", ChannelName.INBOUND_EVENTS_FILE_UPLOAD_CALLBACK, new TypeReference<FileUploadCallback>() {}),

    HMS("hms", ChannelName.INBOUND_EVENTS_HMS, new TypeReference<Hms>() {}),

    DEVICE_REBOOT("device_reboot", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    DRONE_OPEN("drone_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    DRONE_CLOSE("drone_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    DRONE_FORMAT("drone_format", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    DEVICE_FORMAT("device_format", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    COVER_OPEN("cover_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    COVER_CLOSE("cover_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    PUTTER_OPEN("putter_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    PUTTER_CLOSE("putter_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    CHARGE_OPEN("charge_open", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    CHARGE_CLOSE("charge_close", ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, new TypeReference<EventsDataRequest<RemoteDebugProgress>>() {}),

    OTA_PROGRESS("ota_progress", ChannelName.INBOUND_EVENTS_OTA_PROGRESS, new TypeReference<EventsDataRequest<OtaProgress>>() {}),

    FILE_UPLOAD_PROGRESS("fileupload_progress", ChannelName.INBOUND_EVENTS_FILEUPLOAD_PROGRESS, new TypeReference<EventsDataRequest<FileUploadProgress>>() {}),

    HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA("highest_priority_upload_flighttask_media", ChannelName.INBOUND_EVENTS_HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA, new TypeReference<HighestPriorityUploadFlightTaskMedia>() {}),

    FLIGHT_TASK_READY("flighttask_ready", ChannelName.INBOUND_EVENTS_FLIGHTTASK_READY, new TypeReference<FlighttaskReady>() {}),

    FLY_TO_POINT_PROGRESS("fly_to_point_progress", ChannelName.INBOUND_EVENTS_FLY_TO_POINT_PROGRESS, new TypeReference<FlyToPointProgress>() {}),

    TAKE_OFF_TO_POINT_PROGRESS("takeoff_to_point_progress", ChannelName.INBOUND_EVENTS_TAKEOFF_TO_POINT_PROGRESS, new TypeReference<TakeoffToPointProgress>() {}),

    DRC_STATUS_NOTIFY("drc_status_notify", ChannelName.INBOUND_EVENTS_DRC_STATUS_NOTIFY, new TypeReference<DrcStatusNotify>() {}),

    JOYSTICK_INVALID_NOTIFY("joystick_invalid_notify", ChannelName.INBOUND_EVENTS_JOYSTICK_INVALID_NOTIFY, new TypeReference<JoystickInvalidNotify>() {}),

    UNKNOWN("", ChannelName.DEFAULT, new TypeReference<>() {});

    private final String method;

    private final String channelName;

    private final TypeReference classType;

    EventsMethodEnum(String method, String channelName, TypeReference classType) {
        this.method = method;
        this.channelName = channelName;
        this.classType = classType;
    }

    public String getMethod() {
        return method;
    }

    public String getChannelName() {
        return channelName;
    }

    public TypeReference getClassType() {
        return classType;
    }

    public static EventsMethodEnum find(String method) {
        return Arrays.stream(EventsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny().orElse(UNKNOWN);
    }
}
