package com.dji.sample.media.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@TableName(value = "media_file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaFileEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("file_id")
    private String fileId;

    @TableField("file_name")
    private String fileName;

    @TableField("file_path")
    private String filePath;

    @TableField("workspace_id")
    private String workspaceId;

    @TableField("fingerprint")
    private String fingerprint;

    @TableField("tinny_fingerprint")
    private String tinnyFingerprint;

    @TableField("object_key")
    private String objectKey;

    @TableField("sub_file_type")
    private Integer subFileType;

    @TableField("is_original")
    private Boolean isOriginal;

    @TableField("drone")
    private String drone;

    @TableField("payload")
    private String payload;

    @TableField("job_id")
    private String jobId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}

