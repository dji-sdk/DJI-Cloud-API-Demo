package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CapacityDeviceReceiver {

    private String sn;

    private Integer availableVideoNumber;

    private Integer coexistVideoNumberMax;

    private List<CapacityCameraReceiver> cameraList;
}