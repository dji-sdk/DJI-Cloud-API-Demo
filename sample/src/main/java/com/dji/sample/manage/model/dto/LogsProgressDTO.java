package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogsProgressDTO {

    private String deviceSn;

    private String deviceModelDomain;

    private Integer progress;

    private Integer result;

    private Float uploadRate;

    private String status;

}
