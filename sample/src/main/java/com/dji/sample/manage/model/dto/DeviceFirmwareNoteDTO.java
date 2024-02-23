package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFirmwareNoteDTO {

    private String deviceName;

    private String productVersion;

    private String releaseNote;

    private LocalDate releasedTime;
}
