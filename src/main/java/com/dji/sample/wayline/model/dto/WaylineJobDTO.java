package com.dji.sample.wayline.model.dto;

import com.dji.sample.wayline.model.enums.WaylineTaskTypeEnum;
import com.dji.sample.wayline.model.enums.WaylineTemplateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaylineJobDTO {

    private String jobId;

    private String jobName;

    private String fileId;

    private String fileName;

    private String dockSn;

    private String dockName;

    private String workspaceId;

    private WaylineTemplateTypeEnum waylineType;

    private WaylineTaskTypeEnum taskType;

    private LocalDateTime executeTime;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime completedTime;

    private Integer status;

    private Integer progress;

    private String username;

    private Integer code;

    private Integer rthAltitude;

    private Integer outOfControlAction;

    private Integer mediaCount;

    private Integer uploadedCount;

    private Boolean uploading;

    private WaylineTaskConditionDTO conditions;

    private String parentId;
}
