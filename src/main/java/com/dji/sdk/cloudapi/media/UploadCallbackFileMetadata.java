package com.dji.sdk.cloudapi.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "media file metadata")
public class UploadCallbackFileMetadata {

    private Double absoluteAltitude;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssVV")
    private LocalDateTime createdTime;

    private Double gimbalYawDegree;

    private Position shootPosition;

    private Double relativeAltitude;

    public UploadCallbackFileMetadata() {
    }

    @Override
    public String toString() {
        return "MediaFileMetadata{" +
                "absoluteAltitude=" + absoluteAltitude +
                ", createdTime=" + createdTime +
                ", gimbalYawDegree=" + gimbalYawDegree +
                ", shootPosition=" + shootPosition +
                ", relativeAltitude=" + relativeAltitude +
                '}';
    }

    public Double getAbsoluteAltitude() {
        return absoluteAltitude;
    }

    public UploadCallbackFileMetadata setAbsoluteAltitude(Double absoluteAltitude) {
        this.absoluteAltitude = absoluteAltitude;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public UploadCallbackFileMetadata setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Double getGimbalYawDegree() {
        return gimbalYawDegree;
    }

    public UploadCallbackFileMetadata setGimbalYawDegree(Double gimbalYawDegree) {
        this.gimbalYawDegree = gimbalYawDegree;
        return this;
    }

    public Position getShootPosition() {
        return shootPosition;
    }

    public UploadCallbackFileMetadata setShootPosition(Position shootPosition) {
        this.shootPosition = shootPosition;
        return this;
    }

    public Double getRelativeAltitude() {
        return relativeAltitude;
    }

    public UploadCallbackFileMetadata setRelativeAltitude(Double relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
        return this;
    }
}
