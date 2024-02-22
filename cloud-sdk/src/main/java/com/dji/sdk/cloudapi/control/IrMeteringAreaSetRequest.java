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
public class IrMeteringAreaSetRequest extends BaseModel {

    /**
     * Camera enumeration.
     * It is unofficial device_mode_key.
     * The format is *{type-subtype-gimbalindex}*.
     * Please read [Product Supported](https://developer.dji.com/doc/cloud-api-tutorial/en/overview/product-support.html)
     */
    @NotNull
    private PayloadIndex payloadIndex;

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

    /**
     * Temperature measurement area width
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float width;

    /**
     * Temperature measurement area height
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float height;

    public IrMeteringAreaSetRequest() {
    }

    @Override
    public String toString() {
        return "IrMeteringAreaSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public IrMeteringAreaSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Float getX() {
        return x;
    }

    public IrMeteringAreaSetRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public IrMeteringAreaSetRequest setY(Float y) {
        this.y = y;
        return this;
    }

    public Float getWidth() {
        return width;
    }

    public IrMeteringAreaSetRequest setWidth(Float width) {
        this.width = width;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public IrMeteringAreaSetRequest setHeight(Float height) {
        this.height = height;
        return this;
    }
}
