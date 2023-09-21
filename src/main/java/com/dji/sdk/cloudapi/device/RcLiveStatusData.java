package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.livestream.VideoQualityEnum;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class RcLiveStatusData {

    private Boolean status;

    private VideoId videoId;

    private VideoQualityEnum videoQuality;

    public RcLiveStatusData() {
    }

    @Override
    public String toString() {
        return "RcLiveStatusData{" +
                "status=" + status +
                ", videoId=" + videoId +
                ", videoQuality=" + videoQuality +
                '}';
    }

    public Boolean getStatus() {
        return status;
    }

    public RcLiveStatusData setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public RcLiveStatusData setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }

    public VideoQualityEnum getVideoQuality() {
        return videoQuality;
    }

    public RcLiveStatusData setVideoQuality(VideoQualityEnum videoQuality) {
        this.videoQuality = videoQuality;
        return this;
    }
}