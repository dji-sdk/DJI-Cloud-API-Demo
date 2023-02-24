package com.dji.sample.manage.model.param;

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
    private String releaseNote;
    
    @NotNull
    private Boolean status;

    @NotNull
    private List<String> deviceName;
}
