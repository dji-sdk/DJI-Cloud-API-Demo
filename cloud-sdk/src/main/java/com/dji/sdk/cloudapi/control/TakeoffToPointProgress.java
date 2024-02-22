package com.dji.sdk.cloudapi.control;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.cloudapi.wayline.WaylineErrorCodeEnum;
import com.dji.sdk.config.version.CloudSDKVersionEnum;

import java.util.List;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public class TakeoffToPointProgress {

    private WaylineErrorCodeEnum result;

    private TakeoffStatusEnum status;

    private String flightId;

    private String trackId;

    private Integer wayPointIndex;

    /**
     * Remaining mission distance
     * unit: m
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private Float remainingDistance;

    /**
     * Remaining mission time
     * unit: s
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private Integer remainingTime;

    /**
     * Planned trajectory point list
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private List<Point> plannedPathPoints;


    public TakeoffToPointProgress() {
    }

    @Override
    public String toString() {
        return "TakeoffToPointProgress{" +
                "result=" + result +
                ", status=" + status +
                ", flightId='" + flightId + '\'' +
                ", trackId='" + trackId + '\'' +
                ", wayPointIndex=" + wayPointIndex +
                ", remainingDistance=" + remainingDistance +
                ", remainingTime=" + remainingTime +
                ", plannedPathPoints=" + plannedPathPoints +
                '}';
    }

    public WaylineErrorCodeEnum getResult() {
        return result;
    }

    public TakeoffToPointProgress setResult(WaylineErrorCodeEnum result) {
        this.result = result;
        return this;
    }

    public TakeoffStatusEnum getStatus() {
        return status;
    }

    public TakeoffToPointProgress setStatus(TakeoffStatusEnum status) {
        this.status = status;
        return this;
    }

    public String getFlightId() {
        return flightId;
    }

    public TakeoffToPointProgress setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }

    public String getTrackId() {
        return trackId;
    }

    public TakeoffToPointProgress setTrackId(String trackId) {
        this.trackId = trackId;
        return this;
    }

    public Integer getWayPointIndex() {
        return wayPointIndex;
    }

    public TakeoffToPointProgress setWayPointIndex(Integer wayPointIndex) {
        this.wayPointIndex = wayPointIndex;
        return this;
    }

    public Float getRemainingDistance() {
        return remainingDistance;
    }

    public TakeoffToPointProgress setRemainingDistance(Float remainingDistance) {
        this.remainingDistance = remainingDistance;
        return this;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public TakeoffToPointProgress setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
        return this;
    }

    public List<Point> getPlannedPathPoints() {
        return plannedPathPoints;
    }

    public TakeoffToPointProgress setPlannedPathPoints(List<Point> plannedPathPoints) {
        this.plannedPathPoints = plannedPathPoints;
        return this;
    }
}
