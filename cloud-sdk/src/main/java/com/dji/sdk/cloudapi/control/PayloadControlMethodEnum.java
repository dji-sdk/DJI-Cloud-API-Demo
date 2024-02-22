package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
public enum PayloadControlMethodEnum {

    CAMERA_MODE_SWITCH(ControlMethodEnum.CAMERA_MODE_SWITCH, CameraModeSwitchRequest.class),

    CAMERA_PHOTO_TAKE(ControlMethodEnum.CAMERA_PHOTO_TAKE, CameraPhotoTakeRequest.class),

    CAMERA_PHOTO_STOP(ControlMethodEnum.CAMERA_PHOTO_STOP, CameraPhotoStopRequest.class),

    CAMERA_RECORDING_START(ControlMethodEnum.CAMERA_RECORDING_START, CameraRecordingStartRequest.class),

    CAMERA_RECORDING_STOP(ControlMethodEnum.CAMERA_RECORDING_STOP, CameraRecordingStopRequest.class),

    CAMERA_AIM(ControlMethodEnum.CAMERA_AIM, CameraAimRequest.class),

    CAMERA_FOCAL_LENGTH_SET(ControlMethodEnum.CAMERA_FOCAL_LENGTH_SET, CameraFocalLengthSetRequest.class),

    GIMBAL_RESET(ControlMethodEnum.GIMBAL_RESET, GimbalResetRequest.class),

    CAMERA_LOOK_AT(ControlMethodEnum.CAMERA_LOOK_AT, CameraLookAtRequest.class),

    CAMERA_SCREEN_SPLIT(ControlMethodEnum.CAMERA_SCREEN_SPLIT, CameraScreenSplitRequest.class),

    PHOTO_STORAGE_SET(ControlMethodEnum.PHOTO_STORAGE_SET, PhotoStorageSetRequest.class),

    VIDEO_STORAGE_SET(ControlMethodEnum.VIDEO_STORAGE_SET, VideoStorageSetRequest.class),

    CAMERA_EXPOSURE_SET(ControlMethodEnum.CAMERA_EXPOSURE_SET, CameraExposureSetRequest.class),

    CAMERA_EXPOSURE_MODE_SET(ControlMethodEnum.CAMERA_EXPOSURE_MODE_SET, CameraExposureModeSetRequest.class),

    CAMERA_FOCUS_MODE_SET(ControlMethodEnum.CAMERA_FOCUS_MODE_SET, CameraFocusModeSetRequest.class),

    CAMERA_FOCUS_VALUE_SET(ControlMethodEnum.CAMERA_FOCUS_VALUE_SET, CameraFocusValueSetRequest.class),

    IR_METERING_MODE_SET(ControlMethodEnum.IR_METERING_MODE_SET, IrMeteringModeSetRequest.class),

    IR_METERING_POINT_SET(ControlMethodEnum.IR_METERING_POINT_SET, IrMeteringPointSetRequest.class),

    IR_METERING_AREA_SET(ControlMethodEnum.IR_METERING_AREA_SET, IrMeteringAreaSetRequest.class),

    CAMERA_POINT_FOCUS_ACTION(ControlMethodEnum.CAMERA_POINT_FOCUS_ACTION, CameraPointFocusActionRequest.class),

    ;

    private final ControlMethodEnum payloadMethod;

    private final Class<? extends BaseModel> clazz;

    PayloadControlMethodEnum(ControlMethodEnum payloadMethod, Class<? extends BaseModel> clazz) {
        this.payloadMethod = payloadMethod;
        this.clazz = clazz;
    }

    public ControlMethodEnum getPayloadMethod() {
        return payloadMethod;
    }

    public Class<? extends BaseModel> getClazz() {
        return clazz;
    }

    public static PayloadControlMethodEnum find(String method) {
        return Arrays.stream(values()).filter(methodEnum -> methodEnum.payloadMethod.getMethod().equals(method)).findAny()
            .orElseThrow(() -> new CloudSDKException(PayloadControlMethodEnum.class, method));
    }
}
