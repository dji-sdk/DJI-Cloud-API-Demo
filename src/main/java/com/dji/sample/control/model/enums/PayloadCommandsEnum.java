package com.dji.sample.control.model.enums;

import com.dji.sample.control.service.impl.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
public enum PayloadCommandsEnum {

    CAMERA_MODE_SWitCH("camera_mode_switch", CameraModeSwitchImpl.class),

    CAMERA_PHOTO_TAKE("camera_photo_take", CameraPhotoTakeImpl.class),

    CAMERA_RECORDING_START("camera_recording_start", CameraRecordingStartImpl.class),

    CAMERA_RECORDING_STOP("camera_recording_stop", CameraRecordingStopImpl.class),

    CAMERA_AIM("camera_aim", CameraAimImpl.class),

    CAMERA_FOCAL_LENGTH_SET("camera_focal_length_set", CameraFocalLengthSetImpl.class),

    GIMBAL_RESET("gimbal_reset", GimbalResetImpl.class);

    String cmd;

    Class<? extends PayloadCommandsHandler> clazz;

    PayloadCommandsEnum(String cmd, Class<? extends PayloadCommandsHandler> clazz) {
        this.cmd = cmd;
        this.clazz = clazz;
    }

    @JsonValue
    public String getCmd() {
        return cmd;
    }

    public Class<? extends PayloadCommandsHandler> getClazz() {
        return clazz;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PayloadCommandsEnum find(String cmd) {
        return Arrays.stream(values()).filter(cmdEnum -> cmdEnum.cmd.equals(cmd)).findAny().get();
    }
}
