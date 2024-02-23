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
 * @date 2022/9/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("logs_file_index")
public class LogsFileIndexEntity implements Serializable {

    private static final long serialVersionUID = -12L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("boot_index")
    private Integer bootIndex;

    @TableField("file_id")
    private String fileId;

    @TableField("start_time")
    private Long startTime;

    @TableField("end_time")
    private Long endTime;

    @TableField("size")
    private Long size;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("domain")
    private Integer domain;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
