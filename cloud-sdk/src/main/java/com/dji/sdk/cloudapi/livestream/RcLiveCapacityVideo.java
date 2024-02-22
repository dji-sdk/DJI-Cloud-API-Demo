package com.dji.sdk.cloudapi.livestream;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class RcLiveCapacityVideo {

    private String videoIndex;

    private VideoTypeEnum videoType;

    public RcLiveCapacityVideo() {
    }

    @Override
    public String toString() {
        return "RcLiveCapacityVideo{" +
                "videoIndex='" + videoIndex + '\'' +
                ", videoType=" + videoType +
                '}';
    }

    public String getVideoIndex() {
        return videoIndex;
    }

    public RcLiveCapacityVideo setVideoIndex(String videoIndex) {
        this.videoIndex = videoIndex;
        return this;
    }

    public VideoTypeEnum getVideoType() {
        return videoType;
    }

    public RcLiveCapacityVideo setVideoType(VideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }
}