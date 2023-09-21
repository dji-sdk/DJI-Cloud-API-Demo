package com.dji.sdk.cloudapi.control;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class DelayInfoPush {

    private Integer sdrCmdDelay;

    private List<LiveviewDelay> liveviewDelayList;

    public DelayInfoPush() {
    }

    @Override
    public String toString() {
        return "DelayInfoPush{" +
                "sdrCmdDelay=" + sdrCmdDelay +
                ", liveviewDelayList=" + liveviewDelayList +
                '}';
    }

    public Integer getSdrCmdDelay() {
        return sdrCmdDelay;
    }

    public DelayInfoPush setSdrCmdDelay(Integer sdrCmdDelay) {
        this.sdrCmdDelay = sdrCmdDelay;
        return this;
    }

    public List<LiveviewDelay> getLiveviewDelayList() {
        return liveviewDelayList;
    }

    public DelayInfoPush setLiveviewDelayList(List<LiveviewDelay> liveviewDelayList) {
        this.liveviewDelayList = liveviewDelayList;
        return this;
    }
}
