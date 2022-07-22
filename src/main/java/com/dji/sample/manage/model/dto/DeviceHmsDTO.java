package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceHmsDTO implements Cloneable {

    private String hmsId;

    private String tid;

    private String bid;

    private String sn;

    private Integer level;

    private Integer module;

    private String key;

    private String messageZh;

    private String messageEn;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override
    public DeviceHmsDTO clone() {
        try {
            return (DeviceHmsDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            return DeviceHmsDTO.builder()
                    .sn(this.sn)
                    .bid(this.bid)
                    .tid(this.tid)
                    .createTime(this.createTime)
                    .updateTime(this.updateTime)
                    .build();
        }
    }
}
