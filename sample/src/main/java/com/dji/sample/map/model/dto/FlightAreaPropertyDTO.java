package com.dji.sample.map.model.dto;

import com.dji.sdk.cloudapi.flightarea.GeofenceTypeEnum;
import com.dji.sdk.cloudapi.flightarea.GeometrySubTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightAreaPropertyDTO {

    private String elementId;

    private Boolean status;

    private GeofenceTypeEnum type;

    private Float radius;

    private GeometrySubTypeEnum subType;
}
