package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/12
 */
@Schema(description = "Wayline file metadata")
public class WaylineUploadCallbackMetadata {

    /**
     * drone device product enum
     */
    @NotNull
    @Schema(description = "drone device product enum", example = "0-67-0")
    @JsonProperty("drone_model_key")
    private DeviceEnum droneModelKey;

    /**
     * payload device product enum
     */
    @NotNull
    @Size(min = 1)
    @JsonProperty("payload_model_keys")
    @Schema(description = "payload device product enum", example = "[\"1-53-0\"]")
    private List<DeviceEnum> payloadModelKeys;

    /**
     * wayline template collection
     */
    @NotNull
    @Size(min = 1)
    @Schema(description = "wayline template collection", example = "[0]")
    @JsonProperty("template_types")
    private List<WaylineTypeEnum> templateTypes;

    public WaylineUploadCallbackMetadata() {
    }

    @Override
    public String toString() {
        return "WaylineUploadCallbackMetadata{" +
                "droneModelKey='" + droneModelKey + '\'' +
                ", payloadModelKeys=" + payloadModelKeys +
                ", templateTypes=" + templateTypes +
                '}';
    }

    public DeviceEnum getDroneModelKey() {
        return droneModelKey;
    }

    public WaylineUploadCallbackMetadata setDroneModelKey(DeviceEnum droneModelKey) {
        this.droneModelKey = droneModelKey;
        return this;
    }

    public List<DeviceEnum> getPayloadModelKeys() {
        return payloadModelKeys;
    }

    public WaylineUploadCallbackMetadata setPayloadModelKeys(List<DeviceEnum> payloadModelKeys) {
        this.payloadModelKeys = payloadModelKeys;
        return this;
    }

    public List<WaylineTypeEnum> getTemplateTypes() {
        return templateTypes;
    }

    public WaylineUploadCallbackMetadata setTemplateTypes(List<WaylineTypeEnum> templateTypes) {
        this.templateTypes = templateTypes;
        return this;
    }
}
