package com.dji.sdk.cloudapi.device;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.config.version.GatewayTypeEnum;

import java.util.List;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/6
 */
public class DockDronePayload {

    private PayloadIndex payloadIndex;

    private Float gimbalPitch;

    private Float gimbalRoll;

    private Float gimbalYaw;

    private Float measureTargetAltitude;

    private Float measureTargetDistance;

    private Float measureTargetLatitude;

    private Float measureTargetLongitude;

    private MeasureTargetStateEnum measureTargetErrorState;

    private Integer version;

    private ThermalPaletteStyleEnum thermalCurrentPaletteStyle;

    private ThermalGainModeEnum thermalGainMode;

    private Float thermalGlobalTemperatureMax;

    private Float thermalGlobalTemperatureMin;

    private Integer thermalIsothermLowerLimit;

    private SwitchActionEnum thermalIsothermState;

    private Integer thermalIsothermUpperLimit;

    private List<SmartTrackPoint> smartTrackPoint;

    @CloudSDKVersion(include = GatewayTypeEnum.DOCK2)
    private Float zoomFactor;

    public DockDronePayload() {
    }

    @Override
    public String toString() {
        return "DockDronePayload{" +
                "payloadIndex=" + payloadIndex +
                ", gimbalPitch=" + gimbalPitch +
                ", gimbalRoll=" + gimbalRoll +
                ", gimbalYaw=" + gimbalYaw +
                ", measureTargetAltitude=" + measureTargetAltitude +
                ", measureTargetDistance=" + measureTargetDistance +
                ", measureTargetLatitude=" + measureTargetLatitude +
                ", measureTargetLongitude=" + measureTargetLongitude +
                ", measureTargetErrorState=" + measureTargetErrorState +
                ", version=" + version +
                ", thermalCurrentPaletteStyle=" + thermalCurrentPaletteStyle +
                ", thermalGainMode=" + thermalGainMode +
                ", thermalGlobalTemperatureMax=" + thermalGlobalTemperatureMax +
                ", thermalGlobalTemperatureMin=" + thermalGlobalTemperatureMin +
                ", thermalIsothermLowerLimit=" + thermalIsothermLowerLimit +
                ", thermalIsothermState=" + thermalIsothermState +
                ", thermalIsothermUpperLimit=" + thermalIsothermUpperLimit +
                ", smartTrackPoint=" + smartTrackPoint +
                ", zoomFactor=" + zoomFactor +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public DockDronePayload setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Float getGimbalPitch() {
        return gimbalPitch;
    }

    public DockDronePayload setGimbalPitch(Float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
        return this;
    }

    public Float getGimbalRoll() {
        return gimbalRoll;
    }

    public DockDronePayload setGimbalRoll(Float gimbalRoll) {
        this.gimbalRoll = gimbalRoll;
        return this;
    }

    public Float getGimbalYaw() {
        return gimbalYaw;
    }

    public DockDronePayload setGimbalYaw(Float gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
        return this;
    }

    public Float getMeasureTargetAltitude() {
        return measureTargetAltitude;
    }

    public DockDronePayload setMeasureTargetAltitude(Float measureTargetAltitude) {
        this.measureTargetAltitude = measureTargetAltitude;
        return this;
    }

    public Float getMeasureTargetDistance() {
        return measureTargetDistance;
    }

    public DockDronePayload setMeasureTargetDistance(Float measureTargetDistance) {
        this.measureTargetDistance = measureTargetDistance;
        return this;
    }

    public Float getMeasureTargetLatitude() {
        return measureTargetLatitude;
    }

    public DockDronePayload setMeasureTargetLatitude(Float measureTargetLatitude) {
        this.measureTargetLatitude = measureTargetLatitude;
        return this;
    }

    public Float getMeasureTargetLongitude() {
        return measureTargetLongitude;
    }

    public DockDronePayload setMeasureTargetLongitude(Float measureTargetLongitude) {
        this.measureTargetLongitude = measureTargetLongitude;
        return this;
    }

    public MeasureTargetStateEnum getMeasureTargetErrorState() {
        return measureTargetErrorState;
    }

    public DockDronePayload setMeasureTargetErrorState(MeasureTargetStateEnum measureTargetErrorState) {
        this.measureTargetErrorState = measureTargetErrorState;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public DockDronePayload setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public ThermalPaletteStyleEnum getThermalCurrentPaletteStyle() {
        return thermalCurrentPaletteStyle;
    }

    public DockDronePayload setThermalCurrentPaletteStyle(ThermalPaletteStyleEnum thermalCurrentPaletteStyle) {
        this.thermalCurrentPaletteStyle = thermalCurrentPaletteStyle;
        return this;
    }

    public ThermalGainModeEnum getThermalGainMode() {
        return thermalGainMode;
    }

    public DockDronePayload setThermalGainMode(ThermalGainModeEnum thermalGainMode) {
        this.thermalGainMode = thermalGainMode;
        return this;
    }

    public Float getThermalGlobalTemperatureMax() {
        return thermalGlobalTemperatureMax;
    }

    public DockDronePayload setThermalGlobalTemperatureMax(Float thermalGlobalTemperatureMax) {
        this.thermalGlobalTemperatureMax = thermalGlobalTemperatureMax;
        return this;
    }

    public Float getThermalGlobalTemperatureMin() {
        return thermalGlobalTemperatureMin;
    }

    public DockDronePayload setThermalGlobalTemperatureMin(Float thermalGlobalTemperatureMin) {
        this.thermalGlobalTemperatureMin = thermalGlobalTemperatureMin;
        return this;
    }

    public Integer getThermalIsothermLowerLimit() {
        return thermalIsothermLowerLimit;
    }

    public DockDronePayload setThermalIsothermLowerLimit(Integer thermalIsothermLowerLimit) {
        this.thermalIsothermLowerLimit = thermalIsothermLowerLimit;
        return this;
    }

    public SwitchActionEnum getThermalIsothermState() {
        return thermalIsothermState;
    }

    public DockDronePayload setThermalIsothermState(SwitchActionEnum thermalIsothermState) {
        this.thermalIsothermState = thermalIsothermState;
        return this;
    }

    public Integer getThermalIsothermUpperLimit() {
        return thermalIsothermUpperLimit;
    }

    public DockDronePayload setThermalIsothermUpperLimit(Integer thermalIsothermUpperLimit) {
        this.thermalIsothermUpperLimit = thermalIsothermUpperLimit;
        return this;
    }

    public List<SmartTrackPoint> getSmartTrackPoint() {
        return smartTrackPoint;
    }

    public DockDronePayload setSmartTrackPoint(List<SmartTrackPoint> smartTrackPoint) {
        this.smartTrackPoint = smartTrackPoint;
        return this;
    }

    public Float getZoomFactor() {
        return zoomFactor;
    }

    public DockDronePayload setZoomFactor(Float zoomFactor) {
        this.zoomFactor = zoomFactor;
        return this;
    }
}
