package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraExposureModeSetRequest extends BaseModel {

    /**
     * Camera enumeration.
     * It is unofficial device_mode_key.
     * The format is *{type-subtype-gimbalindex}*.
     * Please read [Product Supported](https://developer.dji.com/doc/cloud-api-tutorial/en/overview/product-support.html)
     */
    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private ExposureCameraTypeEnum cameraType;

    @NotNull
    private ExposureModeEnum exposureMode;

    public CameraExposureModeSetRequest() {
    }

    @Override
    public String toString() {
        return "CameraExposureModeSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", exposureMode=" + exposureMode +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraExposureModeSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ExposureCameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraExposureModeSetRequest setCameraType(ExposureCameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public ExposureModeEnum getExposureMode() {
        return exposureMode;
    }

    public CameraExposureModeSetRequest setExposureMode(ExposureModeEnum exposureMode) {
        this.exposureMode = exposureMode;
        return this;
    }
}
