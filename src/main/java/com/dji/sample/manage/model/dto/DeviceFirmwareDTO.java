package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceFirmwareDTO {

    private String firmwareId;

    private String fileName;

    private String productVersion;

    private String objectKey;

    private Long fileSize;

    private String fileMd5;

    private List<String> deviceName;

    private String releaseNote;

    private LocalDate releasedTime;

    private Boolean firmwareStatus;

    private String workspaceId;

    private String username;
}
