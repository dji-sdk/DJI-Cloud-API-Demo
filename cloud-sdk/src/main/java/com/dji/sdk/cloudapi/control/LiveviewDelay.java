package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.VideoId;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class LiveviewDelay {

    private Integer liveviewDelayTime;

    private VideoId videoId;

    public LiveviewDelay() {
    }

    @Override
    public String toString() {
        return "LiveviewDelay{" +
                "liveviewDelayTime=" + liveviewDelayTime +
                ", videoId=" + videoId +
                '}';
    }

    public Integer getLiveviewDelayTime() {
        return liveviewDelayTime;
    }

    public LiveviewDelay setLiveviewDelayTime(Integer liveviewDelayTime) {
        this.liveviewDelayTime = liveviewDelayTime;
        return this;
    }
}
