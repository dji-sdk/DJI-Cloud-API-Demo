package com.dji.sdk.cloudapi.media;

import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
public class UploadCallbackFileExtension {

    private DeviceEnum droneModelKey;

    @JsonProperty("is_original")
    private Boolean original;

    private DeviceEnum payloadModelKey;

    private String flightId;

    public UploadCallbackFileExtension() {
    }

    @Override
    public String toString() {
        return "UploadCallbackFileExtension{" +
                "droneModelKey=" + droneModelKey +
                ", original=" + original +
                ", payloadModelKey=" + payloadModelKey +
                ", flightId='" + flightId + '\'' +
                '}';
    }

    public DeviceEnum getDroneModelKey() {
        return droneModelKey;
    }

    public UploadCallbackFileExtension setDroneModelKey(DeviceEnum droneModelKey) {
        this.droneModelKey = droneModelKey;
        return this;
    }

    public Boolean getOriginal() {
        return original;
    }

    public UploadCallbackFileExtension setOriginal(Boolean original) {
        this.original = original;
        return this;
    }

    public DeviceEnum getPayloadModelKey() {
        return payloadModelKey;
    }

    public UploadCallbackFileExtension setPayloadModelKey(DeviceEnum payloadModelKey) {
        this.payloadModelKey = payloadModelKey;
        return this;
    }

    public String getFlightId() {
        return flightId;
    }

    public UploadCallbackFileExtension setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
