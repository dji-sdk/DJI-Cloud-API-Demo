package com.dji.sample.wayline.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("wayline_job")
public class WaylineJobEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("job_id")
    private String jobId;

    @TableField("name")
    private String name;

    @TableField("file_id")
    private String fileId;

    @TableField("dock_sn")
    private String dockSn;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("task_type")
    private Integer taskType;

    @TableField("wayline_type")
    private Integer waylineType;

    @TableField("username")
    private String username;

    @TableField("execute_time")
    private Long executeTime;

    @TableField("end_time")
    private Long endTime;

    @TableField("error_code")
    private Integer errorCode;

    @TableField("status")
    private Integer status;

    @TableField("rth_altitude")
    private Integer rthAltitude;

    @TableField("out_of_control")
    private Integer outOfControlAction;

    @TableField("media_count")
    private Integer mediaCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField("begin_time")
    private Long beginTime;

    @TableField("completed_time")
    private Long completedTime;

    @TableField("parent_id")
    private String parentId;
}
