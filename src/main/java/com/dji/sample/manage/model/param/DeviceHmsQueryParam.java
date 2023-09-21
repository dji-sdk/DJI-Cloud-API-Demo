package com.dji.sample.manage.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("device_sn")
    private Set<String> deviceSn;

    @JsonProperty("begin_time")
    private Long beginTime;

    @JsonProperty("end_time")
    private Long endTime;

    private String language;

    private String message;

    private Long page;

    @JsonProperty("page_size")
    private Long pageSize;

    private Integer level;

    @JsonProperty("update_time")
    private Long updateTime;
}
