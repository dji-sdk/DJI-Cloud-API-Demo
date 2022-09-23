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
 * @date 2022/9/7
 */
@TableName("manage_device_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceLogsEntity implements Serializable {

    private static final long serialVersionUID = -12L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("logs_id")
    private String logsId;

    @TableField("username")
    private String username;

    @TableField("logs_info")
    private String logsInfo;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("happen_time")
    private Long happenTime;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

}
