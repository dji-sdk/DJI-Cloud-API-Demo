package com.dji.sample.control.model.enums;

import com.dji.sample.control.service.impl.*;
import com.dji.sdk.cloudapi.control.PayloadControlMethodEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
public enum PayloadCommandsEnum {

    CAMERA_MODE_SWitCH(PayloadControlMethodEnum.CAMERA_MODE_SWITCH, CameraModeSwitchImpl.class),

    CAMERA_PHOTO_TAKE(PayloadControlMethodEnum.CAMERA_PHOTO_TAKE, CameraPhotoTakeImpl.class),

    CAMERA_RECORDING_START(PayloadControlMethodEnum.CAMERA_RECORDING_START, CameraRecordingStartImpl.class),

    CAMERA_RECORDING_STOP(PayloadControlMethodEnum.CAMERA_RECORDING_STOP, CameraRecordingStopImpl.class),

    CAMERA_AIM(PayloadControlMethodEnum.CAMERA_AIM, CameraAimImpl.class),

    CAMERA_FOCAL_LENGTH_SET(PayloadControlMethodEnum.CAMERA_FOCAL_LENGTH_SET, CameraFocalLengthSetImpl.class),

    GIMBAL_RESET(PayloadControlMethodEnum.GIMBAL_RESET, GimbalResetImpl.class);

    PayloadControlMethodEnum cmd;

    Class<? extends PayloadCommandsHandler> clazz;

    PayloadCommandsEnum(PayloadControlMethodEnum cmd, Class<? extends PayloadCommandsHandler> clazz) {
        this.cmd = cmd;
        this.clazz = clazz;
    }

    @JsonValue
    public String getMethod() {
        return cmd.getPayloadMethod().getMethod();
    }

    public Class<? extends PayloadCommandsHandler> getClazz() {
        return clazz;
    }

    public PayloadControlMethodEnum getCmd() {
        return cmd;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PayloadCommandsEnum find(String method) {
        return Arrays.stream(values()).filter(methodEnum -> methodEnum.cmd.getPayloadMethod().getMethod().equals(method)).findAny()
                .orElseThrow();
    }
}
