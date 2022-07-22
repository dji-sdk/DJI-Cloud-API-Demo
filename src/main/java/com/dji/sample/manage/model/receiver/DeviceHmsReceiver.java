package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
@Data
public class DeviceHmsReceiver {

    private String code;

    private String deviceType;

    private String domainType;

    private Integer imminent;

    private Integer inTheSky;

    private Integer level;

    private Integer module;

    private HmsArgsReceiver args;
}
