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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "manage_capacity_camera")
public class CapacityCameraEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "device_sn")
    private String deviceSn;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;

    @TableField(value = "camera_index")
    private String cameraIndex;

    @TableField(value = "coexist_video_number_max")
    private Integer coexistVideoNumberMax;

    @TableField(value = "available_video_number")
    private Integer availableVideoNumber;
}