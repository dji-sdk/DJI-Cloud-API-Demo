package com.dji.sample.wayline.model.param;

import com.dji.sample.wayline.model.enums.WaylineTaskTypeEnum;
import com.dji.sample.wayline.model.enums.WaylineTemplateTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
public class CreateJobParam {

    @NotBlank
    private String name;

    @NotBlank
    private String fileId;

    @NotBlank
    private String dockSn;

    @NotNull
    private WaylineTemplateTypeEnum waylineType;

    @NotNull
    private WaylineTaskTypeEnum taskType;

    @Range(min = 20, max = 500)
    @NotNull
    private Integer rthAltitude;

    @NotNull
    @Range(max = 2)
    private Integer outOfControlAction;

    @Range(min = 50, max = 90)
    private Integer minBatteryCapacity;

    private Integer minStorageCapacity;

    private List<Long> taskDays;

    private List<List<Long>> taskPeriods;
}
