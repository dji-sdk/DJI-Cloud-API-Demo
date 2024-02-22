package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceDictionaryDTO {

    private Integer domain;

    private Integer deviceType;

    private Integer subType;

    private String deviceName;

    private String deviceDesc;
}