package com.dji.sdk.cloudapi.wayline;

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
}


