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
@TableName(value = "map_group_element")
public class GroupElementEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("group_id")
    private String groupId;

    @TableField("element_id")
    private String elementId;

    @TableField("element_name")
    private String elementName;

    @TableField("display")
    private Integer display;

    @TableField("element_type")
    private Integer elementType;

    @TableField("username")
    private String username;

    @TableField("color")
    private String color;

    @TableField("clamp_to_ground")
    private Boolean clampToGround;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
