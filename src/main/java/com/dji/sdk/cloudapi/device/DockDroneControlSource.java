package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class DockDroneControlSource {

    private ControlSourceEnum controlSource;

    private Float homeLatitude;

    private Float homeLongitude;

    private Integer lowBatteryWarningThreshold;

    private Integer seriousLowBatteryWarningThreshold;

    private List<DockPayloadControlSource> payloads;

    private Boolean locked;

    private ModeCodeReasonEnum modeCodeReason;

    public DockDroneControlSource() {
    }

    @Override
    public String toString() {
        return "DockDroneControlSource{" +
                "controlSource=" + controlSource +
                ", homeLatitude=" + homeLatitude +
                ", homeLongitude=" + homeLongitude +
                ", lowBatteryWarningThreshold=" + lowBatteryWarningThreshold +
                ", seriousLowBatteryWarningThreshold=" + seriousLowBatteryWarningThreshold +
                ", payloads=" + payloads +
                ", locked=" + locked +
                ", modeCodeReason=" + modeCodeReason +
                '}';
    }

    public ControlSourceEnum getControlSource() {
        return controlSource;
    }

    public DockDroneControlSource setControlSource(ControlSourceEnum controlSource) {
        this.controlSource = controlSource;
        return this;
    }

    public Float getHomeLatitude() {
        return homeLatitude;
    }

    public DockDroneControlSource setHomeLatitude(Float homeLatitude) {
        this.homeLatitude = homeLatitude;
        return this;
    }

    public Float getHomeLongitude() {
        return homeLongitude;
    }

    public DockDroneControlSource setHomeLongitude(Float homeLongitude) {
        this.homeLongitude = homeLongitude;
        return this;
    }

    public Integer getLowBatteryWarningThreshold() {
        return lowBatteryWarningThreshold;
    }

    public DockDroneControlSource setLowBatteryWarningThreshold(Integer lowBatteryWarningThreshold) {
        this.lowBatteryWarningThreshold = lowBatteryWarningThreshold;
        return this;
    }

    public Integer getSeriousLowBatteryWarningThreshold() {
        return seriousLowBatteryWarningThreshold;
    }

    public DockDroneControlSource setSeriousLowBatteryWarningThreshold(Integer seriousLowBatteryWarningThreshold) {
        this.seriousLowBatteryWarningThreshold = seriousLowBatteryWarningThreshold;
        return this;
    }

    public List<DockPayloadControlSource> getPayloads() {
        return payloads;
    }

    public DockDroneControlSource setPayloads(List<DockPayloadControlSource> payloads) {
        this.payloads = payloads;
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public DockDroneControlSource setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public ModeCodeReasonEnum getModeCodeReason() {
        return modeCodeReason;
    }

    public DockDroneControlSource setModeCodeReason(ModeCodeReasonEnum modeCodeReason) {
        this.modeCodeReason = modeCodeReason;
        return this;
    }
}