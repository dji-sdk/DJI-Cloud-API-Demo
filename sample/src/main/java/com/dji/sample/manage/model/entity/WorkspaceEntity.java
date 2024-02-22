package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "manage_workspace")
@Data
public class WorkspaceEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "workspace_id")
    private String workspaceId;

    @TableField(value = "workspace_name")
    private String workspaceName;

    @TableField(value = "workspace_desc")
    private String workspaceDesc;

    @TableField(value = "platform_name")
    private String platformName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField(value = "bind_code")
    private String bindCode;
}
