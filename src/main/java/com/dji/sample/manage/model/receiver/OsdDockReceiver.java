package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.enums.DockDrcStateEnum;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
@Data
public class OsdDockReceiver {

    private NetworkStateReceiver networkState;

    private Integer droneInDock;

    private DroneChargeStateReceiver droneChargeState;

    private Integer rainfall;

    private Float windSpeed;

    private Float environmentTemperature;

    private Float temperature;

    private Integer humidity;

    private Double latitude;

    private Double longitude;

    private Double height;

    private AlternateLandPointReceiver alternateLandPoint;

    private Long firstPowerOn;

    private PositionStateReceiver positionState;

    private StorageReceiver storage;

    private DockModeCodeEnum modeCode;

    private Integer coverState;

    private Integer supplementLightState;

    private Boolean emergencyStopState;

    private Integer airConditionerMode;

    private Integer batteryStoreMode;

    private Integer alarmState;

    private Integer putterState;

    private DockSubDeviceReceiver subDevice;

    private Integer jobNumber;

    private Long accTime;

    private Long activationTime;

    private DeviceMaintainStatusReceiver maintainStatus;

    private Integer electricSupplyVoltage;

    private Integer workingVoltage;

    private Integer workingCurrent;

    private BackupBatteryReceiver backupBattery;

    private DroneBatteryMaintenanceInfoReceiver droneBatteryMaintenanceInfo;

    private Integer flighttaskStepCode;

    private Integer flighttaskPrepareCapacity;

    private DockMediaFileDetailReceiver mediaFileDetail;

    private DockSdrReceiver sdr;

    private DockWirelessLinkReceiver wirelessLink;

    private DockDrcStateEnum drcState;
}
