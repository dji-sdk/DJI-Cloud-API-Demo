package com.dji.sample.manage.model.param;

import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Data
public class DeviceLogsQueryParam {

    private Long page;

    private Long pageSize;

    private Integer status;

    private Long beginTime;

    private Long endTime;

    private String logsInformation;
}
