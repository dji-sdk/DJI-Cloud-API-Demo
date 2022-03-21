package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDeviceDTO {

    private Double latitude;

    private Double longitude;

    private Double altitude;

    private Float attitudeHead;

    private Double elevation;

    private Float horizontalSpeed;

    private Float verticalSpeed;
}
