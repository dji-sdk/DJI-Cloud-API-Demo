package com.dji.sample.manage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "manage_camera_video")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CameraVideoEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "camera_id")
    private Integer cameraId;

    @TableField(value = "video_index")
    private String videoIndex;

    @TableField(value = "video_type")
    private String videoType;
}