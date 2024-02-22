package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.cloudapi.device.VideoId;
import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class LiveLensChangeRequest extends BaseModel {

    @NotNull
    private LensChangeVideoTypeEnum videoType;

    /**
     * The format is #{uav_sn}/#{camera_id}/#{video_index},
     * drone serial number/payload and mounted location enumeration value/payload lens numbering
     */
    @NotNull
    private VideoId videoId;

    public LiveLensChangeRequest() {
    }

    @Override
    public String toString() {
        return "LiveLensChangeRequest{" +
                "videoType=" + videoType +
                ", videoId=" + videoId +
                '}';
    }

    public LensChangeVideoTypeEnum getVideoType() {
        return videoType;
    }

    public LiveLensChangeRequest setVideoType(LensChangeVideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public LiveLensChangeRequest setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }
}
