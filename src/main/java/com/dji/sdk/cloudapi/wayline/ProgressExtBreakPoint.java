package com.dji.sdk.cloudapi.wayline;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class ProgressExtBreakPoint {

    /**
     * Breakpoint index
     */
    private Integer index;

    /**
     * Breakpoint state
     */
    private BreakpointStateEnum state;

    /**
     * Current wayline segment process
     */
    private Float progress;

    /**
     * Wayline ID
     */
    private Integer waylineId;

    /**
     * Break reason
     */
    private FlighttaskBreakReasonEnum breakReason;

    /**
     * Breakpoint latitude
     */
    private Float latitude;

    /**
     * Breakpoint longitude
     */
    private Float longitude;

    /**
     * Breakpoint altitude relative to the Earth's ellipsoid surface
     *
     */
    private Float height;

    /**
     * Yaw angle relative to true north (meridian), with positive values from 0 to 6 o'clock direction and negative values from 6 to 12 o'clock direction
     */
    private Integer attitudeHead;

    public ProgressExtBreakPoint() {}

    @Override
    public String toString() {
        return "FlighttaskBreakPoint{" +
                "index=" + index +
                ", state=" + state +
                ", progress=" + progress +
                ", waylineId=" + waylineId +
                ", breakReason=" + breakReason +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", attitudeHead=" + attitudeHead +
                '}';
    }

    public Integer getIndex() {
        return index;
    }

    public ProgressExtBreakPoint setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public BreakpointStateEnum getState() {
        return state;
    }

    public ProgressExtBreakPoint setState(BreakpointStateEnum state) {
        this.state = state;
        return this;
    }

    public Float getProgress() {
        return progress;
    }

    public ProgressExtBreakPoint setProgress(Float progress) {
        this.progress = progress;
        return this;
    }

    public Integer getWaylineId() {
        return waylineId;
    }

    public ProgressExtBreakPoint setWaylineId(Integer waylineId) {
        this.waylineId = waylineId;
        return this;
    }

    public FlighttaskBreakReasonEnum getBreakReason() {
        return breakReason;
    }

    public ProgressExtBreakPoint setBreakReason(FlighttaskBreakReasonEnum breakReason) {
        this.breakReason = breakReason;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public ProgressExtBreakPoint setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public ProgressExtBreakPoint setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public ProgressExtBreakPoint setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Integer getAttitudeHead() {
        return attitudeHead;
    }

    public ProgressExtBreakPoint setAttitudeHead(Integer attitudeHead) {
        this.attitudeHead = attitudeHead;
        return this;
    }
}