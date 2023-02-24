package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductConfigDTO {

    private String ntpServerHost;

    private String appId;

    private String appKey;

    private String appLicense;
}
