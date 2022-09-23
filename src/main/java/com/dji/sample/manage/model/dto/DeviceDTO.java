package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private Boolean status;

    private Boolean boundStatus;

    private LocalDateTime loginTime;

    private LocalDateTime boundTime;

    private String nickname;

    private String userId;

    private String firmwareVersion;

    private String workspaceName;

    private DeviceDTO children;

    private Integer firmwareStatus;

    private Integer firmwareProgress;
}