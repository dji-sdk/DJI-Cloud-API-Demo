package com.dji.sample.manage.model.receiver;

import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/24
 */
@Data
public class LiveCapacityReceiver {

    private Integer availableVideoNumber;

    private Integer coexistVideoNumberMax;

    private List<CapacityDeviceReceiver> deviceList;
}
