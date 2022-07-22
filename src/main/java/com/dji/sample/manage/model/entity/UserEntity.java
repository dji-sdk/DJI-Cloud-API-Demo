package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "manage_user")
@Data
public class UserEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "workspace_id")
    private String workspaceId;

    @TableField(value = "user_type")
    private Integer userType;

    @TableField(value = "mqtt_username")
    private String mqttUsername;

    @TableField(value = "mqtt_password")
    private String mqttPassword;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
