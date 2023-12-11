package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/27
 */
public class DockPositionState {

    @JsonProperty("is_calibration")
    private Boolean calibration;

    private Integer gpsNumber;

    private PositionFixedEnum isFixed;

    private Integer quality;

    private Integer rtkNumber;

    public DockPositionState() {
    }

    @Override
    public String toString() {
        return "DockPositionState{" +
                "Calibration=" + calibration +
                ", gpsNumber=" + gpsNumber +
                ", isFixed=" + isFixed +
                ", quality=" + quality +
                ", rtkNumber=" + rtkNumber +
                '}';
    }

    public Boolean getCalibration() {
        return calibration;
    }

    public DockPositionState setCalibration(Boolean calibration) {
        this.calibration = calibration;
        return this;
    }

    public Integer getGpsNumber() {
        return gpsNumber;
    }

    public DockPositionState setGpsNumber(Integer gpsNumber) {
        this.gpsNumber = gpsNumber;
        return this;
    }

    public PositionFixedEnum getIsFixed() {
        return isFixed;
    }

    public DockPositionState setIsFixed(PositionFixedEnum isFixed) {
        this.isFixed = isFixed;
        return this;
    }

    public Integer getQuality() {
        return quality;
    }

    public DockPositionState setQuality(Integer quality) {
        this.quality = quality;
        return this;
    }

    public Integer getRtkNumber() {
        return rtkNumber;
    }

    public DockPositionState setRtkNumber(Integer rtkNumber) {
        this.rtkNumber = rtkNumber;
        return this;
    }
}
