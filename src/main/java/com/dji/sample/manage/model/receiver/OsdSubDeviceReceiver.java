package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OsdSubDeviceReceiver {

    private Float attitudeHead;

    private Double attitudePitch;

    private Double attitudeRoll;

    private Double elevation;

    private BatteryReceiver battery;

    private String firmwareVersion;

    private Integer gear;

    private Double height;

    private Double homeDistance;

    private Float horizontalSpeed;

    private Double latitude;

    private Double longitude;

    private Integer modeCode;

    private Double totalFlightDistance;

    private Double totalFlightTime;

    private Float verticalSpeed;

    private Double windDirection;

    private Float windSpeed;

    private PositionStateReceiver positionState;

    private List<OsdPayloadReceiver> payloads;

    private StorageReceiver storage;
}