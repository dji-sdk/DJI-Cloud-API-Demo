package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class OsdDockMaintainStatus {

    private List<DockMaintainStatus> maintainStatusArray;

    public OsdDockMaintainStatus() {
    }

    @Override
    public String toString() {
        return "OsdDroneMaintainStatus{" +
                "maintainStatusArray=" + maintainStatusArray +
                '}';
    }

    public List<DockMaintainStatus> getMaintainStatusArray() {
        return maintainStatusArray;
    }

    public OsdDockMaintainStatus setMaintainStatusArray(List<DockMaintainStatus> maintainStatusArray) {
        this.maintainStatusArray = maintainStatusArray;
        return this;
    }
}
