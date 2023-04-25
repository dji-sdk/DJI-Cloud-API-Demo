package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "manage_device_payload")
public class DevicePayloadEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "payload_sn")
    private String payloadSn;

    @TableField(value = "payload_name")
    private String payloadName;

    @TableField(value = "payload_type")
    private Integer payloadType;

    @TableField(value = "sub_type")
    private Integer subType;

    @TableField(value = "firmware_version")
    private String firmwareVersion;

    @TableField(value = "payload_index")
    private Integer payloadIndex;

    @TableField(value = "device_sn")
    private String deviceSn;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField(value = "payload_desc")
    private String payloadDesc;

    @TableField(value = "control_source")
    private String controlSource;

}