package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The entity class of the device
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "manage_device")
public class DeviceEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "device_sn")
    private String deviceSn;

    @TableField(value = "device_name")
    private String deviceName;

    @TableField(value = "workspace_id")
    private String workspaceId;

    @TableField(value = "device_type")
    private Integer deviceType;

    @TableField(value = "sub_type")
    private Integer subType;

    @TableField(value = "domain")
    private Integer domain;

    @TableField(value = "version")
    private Integer version;

    @TableField(value = "device_index")
    private String deviceIndex;

    @TableField(value = "child_sn")
    private String childSn;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField(value = "device_desc")
    private String deviceDesc;

    @TableField(value = "url_normal")
    private String urlNormal;

    @TableField(value = "url_select")
    private String urlSelect;
}