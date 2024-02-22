package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class DockLiveStatus {

    private List<DockLiveStatusData> liveStatus;

    public DockLiveStatus() {
    }

    @Override
    public String toString() {
        return "DockLiveStatus{" +
                "liveStatus=" + liveStatus +
                '}';
    }

    public List<DockLiveStatusData> getLiveStatus() {
        return liveStatus;
    }

    public DockLiveStatus setLiveStatus(List<DockLiveStatusData> liveStatus) {
        this.liveStatus = liveStatus;
        return this;
    }
}