package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.livestream.VideoQualityEnum;
import com.dji.sdk.cloudapi.livestream.VideoTypeEnum;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class DockLiveStatusData {

    private Boolean status;

    private VideoId videoId;

    private VideoQualityEnum videoQuality;

    private VideoTypeEnum videoType;

    private DockLiveErrorStatus errorStatus;

    public DockLiveStatusData() {
    }

    @Override
    public String toString() {
        return "DockLiveStatusData{" +
                "status=" + status +
                ", videoId=" + videoId +
                ", videoQuality=" + videoQuality +
                ", videoType=" + videoType +
                ", errorStatus=" + errorStatus +
                '}';
    }

    public Boolean getStatus() {
        return status;
    }

    public DockLiveStatusData setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public DockLiveStatusData setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }

    public VideoQualityEnum getVideoQuality() {
        return videoQuality;
    }

    public DockLiveStatusData setVideoQuality(VideoQualityEnum videoQuality) {
        this.videoQuality = videoQuality;
        return this;
    }

    public VideoTypeEnum getVideoType() {
        return videoType;
    }

    public DockLiveStatusData setVideoType(VideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }

    public DockLiveErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public DockLiveStatusData setErrorStatus(DockLiveErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
        return this;
    }
}