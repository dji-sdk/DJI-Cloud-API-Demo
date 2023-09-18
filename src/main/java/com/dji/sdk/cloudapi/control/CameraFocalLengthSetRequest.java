package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public class CameraFocalLengthSetRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private ZoomCameraTypeEnum cameraType;

    @Min(2)
    @Max(200)
    @NotNull
    private Float zoomFactor;

    public CameraFocalLengthSetRequest() {
    }

    @Override
    public String toString() {
        return "CameraFocalLengthSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", zoomFactor=" + zoomFactor +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraFocalLengthSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ZoomCameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraFocalLengthSetRequest setCameraType(ZoomCameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public Float getZoomFactor() {
        return zoomFactor;
    }

    public CameraFocalLengthSetRequest setZoomFactor(Float zoomFactor) {
        this.zoomFactor = zoomFactor;
        return this;
    }
}
