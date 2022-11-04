package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2022/11/3
 */
@Data
public class DroneBatteryMaintenanceInfoReceiver {

    private Integer maintenanceState;

    private Long maintenanceTimeLeft;
}
