package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class OsdDroneMaintainStatus {

    private List<DroneMaintainStatus> maintainStatusArray;

    public OsdDroneMaintainStatus() {
    }

    @Override
    public String toString() {
        return "OsdDroneMaintainStatus{" +
                "maintainStatusArray=" + maintainStatusArray +
                '}';
    }

    public List<DroneMaintainStatus> getMaintainStatusArray() {
        return maintainStatusArray;
    }

    public OsdDroneMaintainStatus setMaintainStatusArray(List<DroneMaintainStatus> maintainStatusArray) {
        this.maintainStatusArray = maintainStatusArray;
        return this;
    }
}
