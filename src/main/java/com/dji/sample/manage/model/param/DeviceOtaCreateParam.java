package com.dji.sample.manage.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceOtaCreateParam {

    private String sn;

    private String productVersion;

    private String fileUrl;

    private String md5;

    private Long fileSize;

    private Integer firmwareUpgradeType;

    private String fileName;
}
