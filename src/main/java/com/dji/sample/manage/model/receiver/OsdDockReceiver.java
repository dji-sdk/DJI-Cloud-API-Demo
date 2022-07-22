package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/11
 */
@Data
public class OsdDockReceiver {

    private NetworkStateReceiver networkState;

    private Integer droneInDock;

    private DroneChargeStateReceiver droneChargeState;

    private Integer rainfall;

    private Float windSpeed;

    private Float environmentTemperature;

    private Integer environmentHumidity;

    private Float temperature;

    private Integer humidity;

    private Double latitude;

    private Double longitude;

    private Double height;

    private AlternateLandPointReceiver alternateLandPoint;

    private Integer jobNumber;

    private Integer accTime;

    private Long firstPowerOn;

    private PositionStateReceiver positionState;

    private StorageReceiver storage;

    private Integer electricSupplyVoltage;

    private Integer workingVoltage;

    private Integer workingCurrent;

    private Integer backupBatteryVoltage;

    private Integer modeCode;

    private Integer coverState;

    private Integer supplementLightState;

    private Integer putterState;

    private DockSubDeviceReceiver subDevice;

}
