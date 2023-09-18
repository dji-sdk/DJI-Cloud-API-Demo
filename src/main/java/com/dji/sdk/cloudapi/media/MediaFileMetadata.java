package com.dji.sdk.cloudapi.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "media file metadata")
public class MediaFileMetadata {

    @JsonProperty("absolute_altitude")
    @NotNull
    @Schema(description = "absolute height", example = "-36.889")
    private Double absoluteAltitude;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssVV")
    @NotNull
    @Schema(description = "media create time", example = "2023-01-01T20:00:00+08:00")
    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    @NotNull
    @JsonProperty("gimbal_yaw_degree")
    @Schema(description = "gimbal yaw degree", example = "-4.3")
    private Double gimbalYawDegree;

    @NotNull
    @JsonProperty("shoot_position")
    @Valid
    @Schema(description = "The latitude and longitude of the drone when the photo was taken")
    private Position shootPosition;

    @NotNull
    @JsonProperty("relative_altitude")
    @Schema(description = "relative altitude", example = "100.001")
    private Double relativeAltitude;

    public MediaFileMetadata() {
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

    public MediaFileMetadata setAbsoluteAltitude(Double absoluteAltitude) {
        this.absoluteAltitude = absoluteAltitude;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public MediaFileMetadata setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Double getGimbalYawDegree() {
        return gimbalYawDegree;
    }

    public MediaFileMetadata setGimbalYawDegree(Double gimbalYawDegree) {
        this.gimbalYawDegree = gimbalYawDegree;
        return this;
    }

    public Position getShootPosition() {
        return shootPosition;
    }

    public MediaFileMetadata setShootPosition(Position shootPosition) {
        this.shootPosition = shootPosition;
        return this;
    }

    public Double getRelativeAltitude() {
        return relativeAltitude;
    }

    public MediaFileMetadata setRelativeAltitude(Double relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
        return this;
    }
}
