package com.dji.sdk.cloudapi.media;

import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "media file fast upload extension data")
public class FastUploadExtension {

    @NotNull
    @JsonProperty("drone_model_key")
    @Schema(description = "drone device product enum")
    private DeviceEnum droneModelKey;

    @JsonProperty("is_original")
    @NotNull
    @Schema(description = "Whether the image is the original image.")
    private Boolean original;

    @NotNull
    @JsonProperty("payload_model_key")
    @Schema(description = "payload device product enum", example = "1-42-0")
    private DeviceEnum payloadModelKey;

    @NotNull
    @JsonProperty("tinny_fingerprint")
    @Schema(description = "tiny fingerprint of the file.", example = "297f490b0252690d3f93841818567cc6_2022_8_31_15_16_16")
    private String tinnyFingerprint;

    @NotNull
    @Schema(description = "drone sn", example = "1AD3CA2VL3LAD6")
    private String sn;

    public FastUploadExtension() {
    }

    @Override
    public String toString() {
        return "FastUploadExtension{" +
                "droneModelKey=" + droneModelKey +
                ", original=" + original +
                ", payloadModelKey=" + payloadModelKey +
                ", tinnyFingerprint='" + tinnyFingerprint + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }

    public DeviceEnum getDroneModelKey() {
        return droneModelKey;
    }

    public FastUploadExtension setDroneModelKey(DeviceEnum droneModelKey) {
        this.droneModelKey = droneModelKey;
        return this;
    }

    public Boolean getOriginal() {
        return original;
    }

    public FastUploadExtension setOriginal(Boolean original) {
        this.original = original;
        return this;
    }

    public DeviceEnum getPayloadModelKey() {
        return payloadModelKey;
    }

    public FastUploadExtension setPayloadModelKey(DeviceEnum payloadModelKey) {
        this.payloadModelKey = payloadModelKey;
        return this;
    }

    public String getTinnyFingerprint() {
        return tinnyFingerprint;
    }

    public FastUploadExtension setTinnyFingerprint(String tinnyFingerprint) {
        this.tinnyFingerprint = tinnyFingerprint;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public FastUploadExtension setSn(String sn) {
        this.sn = sn;
        return this;
    }
}
