/*************************************************
 * @copyright 2017 Flision Corporation Inc.
 * @author: Vincent Chan @ Canton
 * @date: 2024年04月19日
 * @version: 1.0.0
 * @description:
 **************************************************/
package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CameraFrameZoomRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private CameraTypeEnum cameraType;

    @NotNull
    private Boolean locked;

    @Min(0)
    @Max(1)
    private Float x;

    @Min(0)
    @Max(1)
    private Float y;

    @Min(0)
    @Max(1)
    private Float width;

    @Min(0)
    @Max(1)
    private Float height;


    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public void setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
    }

    public CameraTypeEnum getCameraType() {
        return cameraType;
    }

    public void setCameraType(CameraTypeEnum cameraType) {
        this.cameraType = cameraType;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }
}
