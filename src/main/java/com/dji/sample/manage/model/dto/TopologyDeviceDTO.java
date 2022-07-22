package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopologyDeviceDTO {

    private String sn;

    private DeviceModelDTO deviceModel;

    private Boolean onlineStatus;

    private String deviceCallsign;

    private String userId;

    private String userCallsign;

    private IconUrlDTO iconUrls;

    private String model;

    private Boolean boundStatus;

    private String gatewaySn;

    private String domain;
}
