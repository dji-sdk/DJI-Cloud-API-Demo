package com.dji.sample.map.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/23
 */
@TableName("device_flight_area")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFlightAreaEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("file_id")
    private String fileId;

    @TableField("sync_status")
    private String syncStatus;

    @TableField("sync_code")
    private Integer syncCode;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
