package com.dji.sample.wayline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaylineTaskReadyConditionDTO {

    private Integer batteryCapacity;

    private Long beginTime;

    private Long endTime;
}
