package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.livestream.VideoTypeEnum;
import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/25
 */
public class VideoId {

    @NotNull
    private String droneSn;

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private VideoTypeEnum videoType = VideoTypeEnum.NORMAL;

    public VideoId() {
    }

    @JsonCreator
    public VideoId(String videoId) {
        if (!StringUtils.hasText(videoId)) {
            return;
        }
        String[] videoIdArr = Arrays.stream(videoId.split("/")).toArray(String[]::new);
        if (videoIdArr.length != 3) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        this.droneSn = videoIdArr[0];
        this.payloadIndex = new PayloadIndex(videoIdArr[1]);
        this.videoType = VideoTypeEnum.find(videoIdArr[2].split("-")[0]);
    }

    @Override
    @JsonValue
    public String toString() {
        if (Objects.isNull(payloadIndex)) {
            return "";
        }
        return String.format("%s/%s/%s-0", droneSn, payloadIndex.toString(), videoType.getType());
    }

    public String getDroneSn() {
        return droneSn;
    }

    public VideoId setDroneSn(String droneSn) {
        this.droneSn = droneSn;
        return this;
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public VideoId setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public VideoTypeEnum getVideoType() {
        return videoType;
    }

    public VideoId setVideoType(VideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }
}
