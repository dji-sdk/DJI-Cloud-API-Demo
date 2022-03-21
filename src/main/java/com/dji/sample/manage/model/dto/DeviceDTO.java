package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceDTO {

    private String deviceSn;

    private String deviceName;

    private String workspaceId;

    private String deviceIndex;

    private String deviceDesc;

    private String childDeviceSn;

    private String domain;

    private Integer type;

    private Integer subType;

    private List<DeviceDTO> gatewaysList;

    private List<DevicePayloadDTO> payloadsList;

    private IconUrlDTO iconUrl;
}