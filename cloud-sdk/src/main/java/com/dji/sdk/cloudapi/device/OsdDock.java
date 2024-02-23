package com.dji.sdk.cloudapi.device;

import com.dji.sdk.annotations.CloudSDKVersion;
import com.dji.sdk.config.version.CloudSDKVersionEnum;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
public class OsdDock {

    private NetworkState networkState;

    private Boolean droneInDock;

    private DroneChargeState droneChargeState;

    private RainfallEnum rainfall;

    private Float windSpeed;

    private Float environmentTemperature;

    private Float temperature;

    private Integer humidity;

    private Float latitude;

    private Float longitude;

    private Float height;

    private AlternateLandPoint alternateLandPoint;

    private Long firstPowerOn;

    private DockPositionState positionState;

    private Storage storage;

    private DockModeCodeEnum modeCode;

    private CoverStateEnum coverState;

    private Boolean supplementLightState;

    private Boolean emergencyStopState;

    private AirConditioner airConditioner;

    private BatteryStoreModeEnum batteryStoreMode;

    private Boolean alarmState;

    private PutterStateEnum putterState;

    private DockSubDevice subDevice;

    private Integer jobNumber;

    private Long accTime;

    private Long activationTime;

    private OsdDockMaintainStatus maintainStatus;

    private Integer electricSupplyVoltage;

    private Integer workingVoltage;

    private Integer workingCurrent;

    private BackupBattery backupBattery;

    private DroneBatteryMaintenanceInfo droneBatteryMaintenanceInfo;

    private FlighttaskStepCodeEnum flighttaskStepCode;

    private Integer flighttaskPrepareCapacity;

    private MediaFileDetail mediaFileDetail;

    private WirelessLink wirelessLink;

    private DrcStateEnum drcState;

    /**
     * User experience improvement program
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private UserExperienceImprovementEnum userExperienceImprovement;

    public OsdDock() {
    }

    @Override
    public String toString() {
        return "OsdDock{" +
                "networkState=" + networkState +
                ", droneInDock=" + droneInDock +
                ", droneChargeState=" + droneChargeState +
                ", rainfall=" + rainfall +
                ", windSpeed=" + windSpeed +
                ", environmentTemperature=" + environmentTemperature +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", alternateLandPoint=" + alternateLandPoint +
                ", firstPowerOn=" + firstPowerOn +
                ", positionState=" + positionState +
                ", storage=" + storage +
                ", modeCode=" + modeCode +
                ", coverState=" + coverState +
                ", supplementLightState=" + supplementLightState +
                ", emergencyStopState=" + emergencyStopState +
                ", airConditioner=" + airConditioner +
                ", batteryStoreMode=" + batteryStoreMode +
                ", alarmState=" + alarmState +
                ", putterState=" + putterState +
                ", subDevice=" + subDevice +
                ", jobNumber=" + jobNumber +
                ", accTime=" + accTime +
                ", activationTime=" + activationTime +
                ", maintainStatus=" + maintainStatus +
                ", electricSupplyVoltage=" + electricSupplyVoltage +
                ", workingVoltage=" + workingVoltage +
                ", workingCurrent=" + workingCurrent +
                ", backupBattery=" + backupBattery +
                ", droneBatteryMaintenanceInfo=" + droneBatteryMaintenanceInfo +
                ", flighttaskStepCode=" + flighttaskStepCode +
                ", flighttaskPrepareCapacity=" + flighttaskPrepareCapacity +
                ", mediaFileDetail=" + mediaFileDetail +
                ", wirelessLink=" + wirelessLink +
                ", drcState=" + drcState +
                ", userExperienceImprovement=" + userExperienceImprovement +
                '}';
    }

    public NetworkState getNetworkState() {
        return networkState;
    }

    public OsdDock setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
        return this;
    }

    public Boolean getDroneInDock() {
        return droneInDock;
    }

    public OsdDock setDroneInDock(Boolean droneInDock) {
        this.droneInDock = droneInDock;
        return this;
    }

    public DroneChargeState getDroneChargeState() {
        return droneChargeState;
    }

    public OsdDock setDroneChargeState(DroneChargeState droneChargeState) {
        this.droneChargeState = droneChargeState;
        return this;
    }

    public RainfallEnum getRainfall() {
        return rainfall;
    }

    public OsdDock setRainfall(RainfallEnum rainfall) {
        this.rainfall = rainfall;
        return this;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public OsdDock setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public Float getEnvironmentTemperature() {
        return environmentTemperature;
    }

    public OsdDock setEnvironmentTemperature(Float environmentTemperature) {
        this.environmentTemperature = environmentTemperature;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public OsdDock setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public OsdDock setHumidity(Integer humidity) {
        this.humidity = humidity;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public OsdDock setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public OsdDock setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public OsdDock setHeight(Float height) {
        this.height = height;
        return this;
    }

    public AlternateLandPoint getAlternateLandPoint() {
        return alternateLandPoint;
    }

    public OsdDock setAlternateLandPoint(AlternateLandPoint alternateLandPoint) {
        this.alternateLandPoint = alternateLandPoint;
        return this;
    }

    public Long getFirstPowerOn() {
        return firstPowerOn;
    }

    public OsdDock setFirstPowerOn(Long firstPowerOn) {
        this.firstPowerOn = firstPowerOn;
        return this;
    }

    public DockPositionState getPositionState() {
        return positionState;
    }

    public OsdDock setPositionState(DockPositionState positionState) {
        this.positionState = positionState;
        return this;
    }

    public Storage getStorage() {
        return storage;
    }

    public OsdDock setStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    public DockModeCodeEnum getModeCode() {
        return modeCode;
    }

    public OsdDock setModeCode(DockModeCodeEnum modeCode) {
        this.modeCode = modeCode;
        return this;
    }

    public CoverStateEnum getCoverState() {
        return coverState;
    }

    public OsdDock setCoverState(CoverStateEnum coverState) {
        this.coverState = coverState;
        return this;
    }

    public Boolean getSupplementLightState() {
        return supplementLightState;
    }

    public OsdDock setSupplementLightState(Boolean supplementLightState) {
        this.supplementLightState = supplementLightState;
        return this;
    }

    public Boolean getEmergencyStopState() {
        return emergencyStopState;
    }

    public OsdDock setEmergencyStopState(Boolean emergencyStopState) {
        this.emergencyStopState = emergencyStopState;
        return this;
    }

    public AirConditioner getAirConditioner() {
        return airConditioner;
    }

    public OsdDock setAirConditioner(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
        return this;
    }

    public BatteryStoreModeEnum getBatteryStoreMode() {
        return batteryStoreMode;
    }

    public OsdDock setBatteryStoreMode(BatteryStoreModeEnum batteryStoreMode) {
        this.batteryStoreMode = batteryStoreMode;
        return this;
    }

    public Boolean getAlarmState() {
        return alarmState;
    }

    public OsdDock setAlarmState(Boolean alarmState) {
        this.alarmState = alarmState;
        return this;
    }

    public PutterStateEnum getPutterState() {
        return putterState;
    }

    public OsdDock setPutterState(PutterStateEnum putterState) {
        this.putterState = putterState;
        return this;
    }

    public DockSubDevice getSubDevice() {
        return subDevice;
    }

    public OsdDock setSubDevice(DockSubDevice subDevice) {
        this.subDevice = subDevice;
        return this;
    }

    public Integer getJobNumber() {
        return jobNumber;
    }

    public OsdDock setJobNumber(Integer jobNumber) {
        this.jobNumber = jobNumber;
        return this;
    }

    public Long getAccTime() {
        return accTime;
    }

    public OsdDock setAccTime(Long accTime) {
        this.accTime = accTime;
        return this;
    }

    public Long getActivationTime() {
        return activationTime;
    }

    public OsdDock setActivationTime(Long activationTime) {
        this.activationTime = activationTime;
        return this;
    }

    public OsdDockMaintainStatus getMaintainStatus() {
        return maintainStatus;
    }

    public OsdDock setMaintainStatus(OsdDockMaintainStatus maintainStatus) {
        this.maintainStatus = maintainStatus;
        return this;
    }

    public Integer getElectricSupplyVoltage() {
        return electricSupplyVoltage;
    }

    public OsdDock setElectricSupplyVoltage(Integer electricSupplyVoltage) {
        this.electricSupplyVoltage = electricSupplyVoltage;
        return this;
    }

    public Integer getWorkingVoltage() {
        return workingVoltage;
    }

    public OsdDock setWorkingVoltage(Integer workingVoltage) {
        this.workingVoltage = workingVoltage;
        return this;
    }

    public Integer getWorkingCurrent() {
        return workingCurrent;
    }

    public OsdDock setWorkingCurrent(Integer workingCurrent) {
        this.workingCurrent = workingCurrent;
        return this;
    }

    public BackupBattery getBackupBattery() {
        return backupBattery;
    }

    public OsdDock setBackupBattery(BackupBattery backupBattery) {
        this.backupBattery = backupBattery;
        return this;
    }

    public DroneBatteryMaintenanceInfo getDroneBatteryMaintenanceInfo() {
        return droneBatteryMaintenanceInfo;
    }

    public OsdDock setDroneBatteryMaintenanceInfo(DroneBatteryMaintenanceInfo droneBatteryMaintenanceInfo) {
        this.droneBatteryMaintenanceInfo = droneBatteryMaintenanceInfo;
        return this;
    }

    public FlighttaskStepCodeEnum getFlighttaskStepCode() {
        return flighttaskStepCode;
    }

    public OsdDock setFlighttaskStepCode(FlighttaskStepCodeEnum flighttaskStepCode) {
        this.flighttaskStepCode = flighttaskStepCode;
        return this;
    }

    public Integer getFlighttaskPrepareCapacity() {
        return flighttaskPrepareCapacity;
    }

    public OsdDock setFlighttaskPrepareCapacity(Integer flighttaskPrepareCapacity) {
        this.flighttaskPrepareCapacity = flighttaskPrepareCapacity;
        return this;
    }

    public MediaFileDetail getMediaFileDetail() {
        return mediaFileDetail;
    }

    public OsdDock setMediaFileDetail(MediaFileDetail mediaFileDetail) {
        this.mediaFileDetail = mediaFileDetail;
        return this;
    }

    public WirelessLink getWirelessLink() {
        return wirelessLink;
    }

    public OsdDock setWirelessLink(WirelessLink wirelessLink) {
        this.wirelessLink = wirelessLink;
        return this;
    }

    public DrcStateEnum getDrcState() {
        return drcState;
    }

    public OsdDock setDrcState(DrcStateEnum drcState) {
        this.drcState = drcState;
        return this;
    }

    public UserExperienceImprovementEnum getUserExperienceImprovement() {
        return userExperienceImprovement;
    }

    public OsdDock setUserExperienceImprovement(UserExperienceImprovementEnum userExperienceImprovement) {
        this.userExperienceImprovement = userExperienceImprovement;
        return this;
    }
}
