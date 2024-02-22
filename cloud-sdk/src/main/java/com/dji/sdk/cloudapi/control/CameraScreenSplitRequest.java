package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public class CameraScreenSplitRequest extends BaseModel {

    /**
     * Camera enumeration.
     * It is unofficial device_mode_key.
     * The format is *{type-subtype-gimbalindex}*.
     * Please read [Product Supported](https://developer.dji.com/doc/cloud-api-tutorial/en/overview/product-support.html)
     */
    @NotNull
    private PayloadIndex payloadIndex;

    /**
     * Whether enable the screen split
     */
    @NotNull
    private Boolean enable;

    public CameraScreenSplitRequest() {
    }

    @Override
    public String toString() {
        return "CameraScreenSplitRequest{" +
                "payloadIndex=" + payloadIndex +
                ", enable=" + enable +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraScreenSplitRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Boolean getEnable() {
        return enable;
    }

    public CameraScreenSplitRequest setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }
}
