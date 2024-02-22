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
public class CameraAimRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private CameraTypeEnum cameraType;

    /**
     * true: Lock the gimbal, the gimbal and the drone rotate together.
     * false: Only the gimbal rotates, but the drone does not.
     */
    @NotNull
    private Boolean locked;

    /**
     * upper left corner as center point
     */
    @Min(0)
    @Max(1)
    private Float x;

    @Min(0)
    @Max(1)
    private Float y;

    public CameraAimRequest() {
    }

    @Override
    public String toString() {
        return "CameraAimRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", locked=" + locked +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraAimRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public CameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraAimRequest setCameraType(CameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public CameraAimRequest setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public Float getX() {
        return x;
    }

    public CameraAimRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public CameraAimRequest setY(Float y) {
        this.y = y;
        return this;
    }
}
