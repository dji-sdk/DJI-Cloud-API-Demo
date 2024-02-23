package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/6
 */
public class RcDronePayload {

    private PayloadIndex payloadIndex;

    private Float gimbalPitch;

    private Float gimbalRoll;

    private Float gimbalYaw;

    private Float measureTargetAltitude;

    private Float measureTargetDistance;

    private Float measureTargetLatitude;

    private Float measureTargetLongitude;

    private MeasureTargetStateEnum measureTargetErrorState;

    private List<SmartTrackPoint> smartTrackPoint;

    public RcDronePayload() {
    }

    @Override
    public String toString() {
        return "RcDronePayload{" +
                "payloadIndex=" + payloadIndex +
                ", gimbalPitch=" + gimbalPitch +
                ", gimbalRoll=" + gimbalRoll +
                ", gimbalYaw=" + gimbalYaw +
                ", measureTargetAltitude=" + measureTargetAltitude +
                ", measureTargetDistance=" + measureTargetDistance +
                ", measureTargetLatitude=" + measureTargetLatitude +
                ", measureTargetLongitude=" + measureTargetLongitude +
                ", measureTargetErrorState=" + measureTargetErrorState +
                ", smartTrackPoint=" + smartTrackPoint +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public RcDronePayload setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Float getGimbalPitch() {
        return gimbalPitch;
    }

    public RcDronePayload setGimbalPitch(Float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
        return this;
    }

    public Float getGimbalRoll() {
        return gimbalRoll;
    }

    public RcDronePayload setGimbalRoll(Float gimbalRoll) {
        this.gimbalRoll = gimbalRoll;
        return this;
    }

    public Float getGimbalYaw() {
        return gimbalYaw;
    }

    public RcDronePayload setGimbalYaw(Float gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
        return this;
    }

    public Float getMeasureTargetAltitude() {
        return measureTargetAltitude;
    }

    public RcDronePayload setMeasureTargetAltitude(Float measureTargetAltitude) {
        this.measureTargetAltitude = measureTargetAltitude;
        return this;
    }

    public Float getMeasureTargetDistance() {
        return measureTargetDistance;
    }

    public RcDronePayload setMeasureTargetDistance(Float measureTargetDistance) {
        this.measureTargetDistance = measureTargetDistance;
        return this;
    }

    public Float getMeasureTargetLatitude() {
        return measureTargetLatitude;
    }

    public RcDronePayload setMeasureTargetLatitude(Float measureTargetLatitude) {
        this.measureTargetLatitude = measureTargetLatitude;
        return this;
    }

    public Float getMeasureTargetLongitude() {
        return measureTargetLongitude;
    }

    public RcDronePayload setMeasureTargetLongitude(Float measureTargetLongitude) {
        this.measureTargetLongitude = measureTargetLongitude;
        return this;
    }

    public MeasureTargetStateEnum getMeasureTargetErrorState() {
        return measureTargetErrorState;
    }

    public RcDronePayload setMeasureTargetErrorState(MeasureTargetStateEnum measureTargetErrorState) {
        this.measureTargetErrorState = measureTargetErrorState;
        return this;
    }

    public List<SmartTrackPoint> getSmartTrackPoint() {
        return smartTrackPoint;
    }

    public RcDronePayload setSmartTrackPoint(List<SmartTrackPoint> smartTrackPoint) {
        this.smartTrackPoint = smartTrackPoint;
        return this;
    }
}
