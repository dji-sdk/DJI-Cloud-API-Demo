package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.dto.DevicePayloadReceiver;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceBasicReceiver {

    private String deviceSn;

    private String controlSource;

    private Float homeLatitude;

    private Float homeLongitude;

    private Integer lowBatteryWarningThreshold;

    private Integer seriousLowBatteryWarningThreshold;

    private List<DevicePayloadReceiver> payloads;
}