package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraPointFocusActionRequest extends BaseModel {

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

    /**
     * The coordinate x of the temperature measurement point is the upper left corner of the lens as the coordinate center point, and the horizontal direction is x.
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float x;

    /**
     * The coordinate y of the temperature measurement point is the upper left corner of the lens as the coordinate center point, and the vertical direction is y.
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float y;

    public CameraPointFocusActionRequest() {
    }

    @Override
    public String toString() {
        return "CameraPointFocusActionRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraPointFocusActionRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ExposureCameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraPointFocusActionRequest setCameraType(ExposureCameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public Float getX() {
        return x;
    }

    public CameraPointFocusActionRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public CameraPointFocusActionRequest setY(Float y) {
        this.y = y;
        return this;
    }
}
