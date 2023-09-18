package com.dji.sample.wayline.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaylineTaskCreateDTO {

    private String flightId;

    private Integer taskType;

    private Integer waylineType;

    private Long executeTime;

    private WaylineTaskFileDTO file;

    private Integer rthAltitude;

    private Integer outOfControlAction;

    private WaylineTaskReadyConditionDTO readyConditions;

    private WaylineTaskExecutableConditionDTO executableConditions;
}
