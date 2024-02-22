package com.dji.sample.manage.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/1
 */
@Data
public class DeviceFirmwareUploadParam {

    @NotNull
    @JsonProperty("release_note")
    private String releaseNote;
    
    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("device_name")
    private List<String> deviceName;
}
