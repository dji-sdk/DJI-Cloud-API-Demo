package com.dji.sample.manage.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author sean
 * @version 1.3
 * @date 2022/12/6
 */
@Data
public class DeviceFirmwareUpdateParam {

    @NotNull
    private Boolean status;
}
