package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/21
 */
@Data
@TableName("manage_firmware_model")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FirmwareModelEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("firmware_id")
    private String firmwareId;

    @TableField("device_name")
    private String deviceName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
