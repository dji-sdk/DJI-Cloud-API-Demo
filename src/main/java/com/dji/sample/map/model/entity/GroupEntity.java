package com.dji.sample.map.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "map_group")
public class GroupEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("group_id")
    private String groupId;

    @TableField("group_name")
    private String groupName;

    @TableField("group_type")
    private Integer groupType;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("is_distributed")
    private Boolean isDistributed;

    @TableField("is_lock")
    private Boolean isLock;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
