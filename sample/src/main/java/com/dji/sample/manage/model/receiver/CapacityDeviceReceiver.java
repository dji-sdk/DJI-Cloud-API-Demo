package com.dji.sample.manage.model.receiver;

import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
public class CapacityDeviceReceiver {

    private String sn;

    private Integer availableVideoNumber;

    private Integer coexistVideoNumberMax;

    private List<CapacityCameraReceiver> cameraList;

}