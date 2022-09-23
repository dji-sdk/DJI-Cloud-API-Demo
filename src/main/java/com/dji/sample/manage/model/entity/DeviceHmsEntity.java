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
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "manage_device_hms")
public class DeviceHmsEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = -12L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("hms_id")
    private String hmsId;

    @TableField("tid")
    private String tid;

    @TableField("bid")
    private String bid;

    @TableField("sn")
    private String sn;

    @TableField("level")
    private Integer level;

    @TableField("module")
    private Integer module;

    @TableField("hms_key")
    private String hmsKey;

    @TableField("message_zh")
    private String messageZh;

    @TableField("message_en")
    private String messageEn;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime;

    @Override
    public DeviceHmsEntity clone() {
        try {
            return (DeviceHmsEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            return DeviceHmsEntity.builder()
                    .bid(this.getBid())
                    .tid(this.getTid())
                    .createTime(this.getCreateTime())
                    .updateTime(this.getUpdateTime())
                    .sn(this.getSn())
                    .build();
        }
    }
}
