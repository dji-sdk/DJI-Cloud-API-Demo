package com.dji.sample.wayline.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Data
@TableName("wayline_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaylineFileEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("wayline_id")
    private String waylineId;

    @TableField("drone_model_key")
    private String droneModelKey;

    @TableField("payload_model_keys")
    private String payloadModelKeys;

    @TableField("sign")
    private String sign;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("favorited")
    private Boolean favorited;

    @TableField("template_types")
    private String templateTypes;

    @TableField("object_key")
    private String objectKey;

    @TableField("user_name")
    private String username;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

}
