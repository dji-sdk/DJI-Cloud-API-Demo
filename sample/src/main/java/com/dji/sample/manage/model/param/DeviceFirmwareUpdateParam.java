package com.dji.sample.manage.model.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

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
