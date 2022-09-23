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
@TableName("logs_file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogsFileEntity implements Serializable {

    private static final long serialVersionUID = -12L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("file_id")
    private String fileId;

    @TableField("name")
    private String name;

    @TableField("size")
    private Long size;

    @TableField("logs_id")
    private String logsId;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("fingerprint")
    private String fingerprint;

    @TableField("object_key")
    private String objectKey;

    @TableField("status")
    private Boolean status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

}
