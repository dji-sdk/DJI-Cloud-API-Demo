package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("manage_device_firmware")
public class DeviceFirmwareEntity implements Serializable {

    private static final long serialVersionUID = -12L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("firmware_id")
    private String firmwareId;

    @TableField("file_name")
    private String fileName;

    @TableField("firmware_version")
    private String firmwareVersion;

    @TableField("object_key")
    private String objectKey;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_md5")
    private String fileMd5;

    @TableField(exist = false)
    private String deviceName;

    @TableField("release_note")
    private String releaseNote;

    @TableField("release_date")
    private Long releaseDate;

    @TableField("status")
    private Boolean status;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("user_name")
    private String username;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

}
