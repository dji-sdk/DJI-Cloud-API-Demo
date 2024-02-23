package com.dji.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class DockLiveCapacityVideo {

    private String videoIndex;

    private VideoTypeEnum videoType;

    private List<VideoTypeEnum> switchableVideoTypes;

    public DockLiveCapacityVideo() {
    }

    @Override
    public String toString() {
        return "DockLiveCapacityVideo{" +
                "videoIndex='" + videoIndex + '\'' +
                ", videoType=" + videoType +
                ", switchableVideoTypes=" + switchableVideoTypes +
                '}';
    }

    public String getVideoIndex() {
        return videoIndex;
    }

    public DockLiveCapacityVideo setVideoIndex(String videoIndex) {
        this.videoIndex = videoIndex;
        return this;
    }

    public VideoTypeEnum getVideoType() {
        return videoType;
    }

    public DockLiveCapacityVideo setVideoType(VideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }

    public List<VideoTypeEnum> getSwitchableVideoTypes() {
        return switchableVideoTypes;
    }

    public DockLiveCapacityVideo setSwitchableVideoTypes(List<VideoTypeEnum> switchableVideoTypes) {
        this.switchableVideoTypes = switchableVideoTypes;
        return this;
    }
}