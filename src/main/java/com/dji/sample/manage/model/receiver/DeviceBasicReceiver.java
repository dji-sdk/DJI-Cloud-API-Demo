package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceBasicReceiver {

    private String controlSource;

    private String deviceSn;

    private Double homeLatitude;

    private Double homeLongitude;

    private Integer lowBatteryWarningThreshold;

    private Integer seriousLowBatteryWarningThreshold;

    private List<DevicePayloadReceiver> payloads;
}