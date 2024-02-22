package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.config.version.CloudSDKVersionEnum;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
public class FlighttaskProgressExt {

    private Integer currentWaypointIndex;

    private Integer mediaCount;

    private String flightId;

    private String trackId;

    private ProgressExtBreakPoint breakPoint;

    /**
     * Wayline mission state
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private WaylineMissionStateEnum waylineMissionState;

    /**
     * This includes the transitional phase of entering the flight path.
     * For example, 0 indicates that the spacecraft is entering or already executing the first route.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private Integer waylineId;

    public FlighttaskProgressExt() {
    }

    @Override
    public String toString() {
        return "FlighttaskProgressExt{" +
                "currentWaypointIndex=" + currentWaypointIndex +
                ", mediaCount=" + mediaCount +
                ", flightId='" + flightId + '\'' +
                ", trackId='" + trackId + '\'' +
                ", breakPoint=" + breakPoint +
                ", waylineMissionState=" + waylineMissionState +
                ", waylineId=" + waylineId +
                '}';
    }

    public Integer getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }

    public FlighttaskProgressExt setCurrentWaypointIndex(Integer currentWaypointIndex) {
        this.currentWaypointIndex = currentWaypointIndex;
        return this;
    }

    public Integer getMediaCount() {
        return mediaCount;
    }

    public FlighttaskProgressExt setMediaCount(Integer mediaCount) {
        this.mediaCount = mediaCount;
        return this;
    }

    public String getFlightId() {
        return flightId;
    }

    public FlighttaskProgressExt setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }

    public String getTrackId() {
        return trackId;
    }

    public FlighttaskProgressExt setTrackId(String trackId) {
        this.trackId = trackId;
        return this;
    }

    public ProgressExtBreakPoint getBreakPoint() {
        return breakPoint;
    }

    public FlighttaskProgressExt setBreakPoint(ProgressExtBreakPoint breakPoint) {
        this.breakPoint = breakPoint;
        return this;
    }

    public WaylineMissionStateEnum getWaylineMissionState() {
        return waylineMissionState;
    }

    public FlighttaskProgressExt setWaylineMissionState(WaylineMissionStateEnum waylineMissionState) {
        this.waylineMissionState = waylineMissionState;
        return this;
    }

    public Integer getWaylineId() {
        return waylineId;
    }

    public FlighttaskProgressExt setWaylineId(Integer waylineId) {
        this.waylineId = waylineId;
        return this;
    }
}


