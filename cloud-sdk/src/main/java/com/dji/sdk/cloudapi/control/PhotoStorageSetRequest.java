package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public class PhotoStorageSetRequest extends BaseModel {

    /**
     * Camera enumeration.
     * It is unofficial device_mode_key.
     * The format is *{type-subtype-gimbalindex}*.
     * Please read [Product Supported](https://developer.dji.com/doc/cloud-api-tutorial/en/overview/product-support.html)
     */
    @NotNull
    private PayloadIndex payloadIndex;

    /**
     * Photo storage type. Multi-selection.
     */
    @NotNull
    @Size(min = 1)
    private List<LensStorageSettingsEnum> photoStorageSettings;

    public PhotoStorageSetRequest() {
    }

    @Override
    public String toString() {
        return "PhotoStorageSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", photoStorageSettings=" + photoStorageSettings +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public PhotoStorageSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public List<LensStorageSettingsEnum> getPhotoStorageSettings() {
        return photoStorageSettings;
    }

    public PhotoStorageSetRequest setPhotoStorageSettings(List<LensStorageSettingsEnum> photoStorageSettings) {
        this.photoStorageSettings = photoStorageSettings;
        return this;
    }
}
