package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class RcLiveStatus {

    private List<RcLiveStatusData> liveStatus;

    public RcLiveStatus() {
    }

    @Override
    public String toString() {
        return "RcLiveStatus{" +
                "liveStatus=" + liveStatus +
                '}';
    }

    public List<RcLiveStatusData> getLiveStatus() {
        return liveStatus;
    }

    public RcLiveStatus setLiveStatus(List<RcLiveStatusData> liveStatus) {
        this.liveStatus = liveStatus;
        return this;
    }
}