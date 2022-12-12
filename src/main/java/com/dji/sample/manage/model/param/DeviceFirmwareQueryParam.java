package com.dji.sample.manage.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceFirmwareQueryParam {

    private String deviceName;

    private String productVersion;

    private Boolean status;

    @NotNull
    private Long page;

    @NotNull
    private Long pageSize;
}
