package com.dji.sample.map.model.dto;

import com.dji.sample.map.model.enums.FlightAreaOpertaionEnum;
import com.dji.sdk.cloudapi.flightarea.GeofenceTypeEnum;
import lombok.*;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlightAreaWs {

    private FlightAreaOpertaionEnum operation;

    private String areaId;

    private String name;

    private GeofenceTypeEnum type;

    private FlightAreaContent content;

    private Boolean status;

    private String username;

    private Long createTime;

    private Long updateTime;

}
