package com.dji.sample.manage.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHmsQueryParam {

    private Set<String> deviceSn;

    private Long beginTime;

    private Long endTime;

    private String language;

    private String message;

    private Long page;

    private Long pageSize;

    private Integer level;

    private Long updateTime;
}
