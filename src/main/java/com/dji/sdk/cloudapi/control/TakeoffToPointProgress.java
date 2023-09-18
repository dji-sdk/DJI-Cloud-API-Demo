package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.wayline.WaylineErrorCodeEnum;

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
}
