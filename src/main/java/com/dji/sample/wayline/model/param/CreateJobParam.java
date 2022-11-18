package com.dji.sample.wayline.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @Range(max = 0)
    @NotNull
    private Integer waylineType;

    @Range(max = 1)
    @NotNull
    private Integer taskType;

    private Long executeTime;

    @Range(min = 20, max = 500)
    @NotNull
    private Integer rthAltitude;

    @NotNull
    @Range(max = 2)
    private Integer outOfControlAction;
}
