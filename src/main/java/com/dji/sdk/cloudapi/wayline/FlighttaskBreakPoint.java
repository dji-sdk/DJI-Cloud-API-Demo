package com.dji.sdk.cloudapi.wayline;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskBreakPoint {

    /**
     * Breakpoint index
     */
    @NotNull
    @Min(0)
    private Integer index;

    /**
     * Breakpoint state
     */
    @NotNull
    private BreakpointStateEnum state;

    /**
     * Current wayline segment process
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float progress;

    /**
     * Wayline ID
     */
    private Integer waylineID;

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

    public FlighttaskBreakPoint() {}

    @Override
    public String toString() {
        return "FlighttaskBreakPoint{" +
                "index=" + index +
                ", state=" + state +
                ", progress=" + progress +
                ", waylineID=" + waylineID +
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

    public FlighttaskBreakPoint setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public BreakpointStateEnum getState() {
        return state;
    }

    public FlighttaskBreakPoint setState(BreakpointStateEnum state) {
        this.state = state;
        return this;
    }

    public Float getProgress() {
        return progress;
    }

    public FlighttaskBreakPoint setProgress(Float progress) {
        this.progress = progress;
        return this;
    }

    public Integer getWaylineID() {
        return waylineID;
    }

    public FlighttaskBreakPoint setWaylineID(Integer waylineID) {
        this.waylineID = waylineID;
        return this;
    }

    public FlighttaskBreakReasonEnum getBreakReason() {
        return breakReason;
    }

    public FlighttaskBreakPoint setBreakReason(FlighttaskBreakReasonEnum breakReason) {
        this.breakReason = breakReason;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public FlighttaskBreakPoint setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public FlighttaskBreakPoint setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public FlighttaskBreakPoint setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Integer getAttitudeHead() {
        return attitudeHead;
    }

    public FlighttaskBreakPoint setAttitudeHead(Integer attitudeHead) {
        this.attitudeHead = attitudeHead;
        return this;
    }
}