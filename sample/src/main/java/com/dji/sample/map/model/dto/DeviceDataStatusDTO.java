package com.dji.sample.map.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataStatusDTO {

    private String deviceSn;

    private String nickname;

    private String deviceName;

    private Boolean online;

    private DeviceFlightAreaDTO flightAreaStatus;
}
