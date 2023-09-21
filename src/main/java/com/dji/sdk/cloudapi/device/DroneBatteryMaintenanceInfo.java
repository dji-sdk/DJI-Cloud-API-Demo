package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.4
 * @date 2022/11/3
 */
public class DroneBatteryMaintenanceInfo {

    private List<DroneBatteryMaintenance> batteries;

    public DroneBatteryMaintenanceInfo() {
    }

    @Override
    public String toString() {
        return "DroneBatteryMaintenanceInfo{" +
                "batteries=" + batteries +
                '}';
    }

    public List<DroneBatteryMaintenance> getBatteries() {
        return batteries;
    }

    public DroneBatteryMaintenanceInfo setBatteries(List<DroneBatteryMaintenance> batteries) {
        this.batteries = batteries;
        return this;
    }
}
