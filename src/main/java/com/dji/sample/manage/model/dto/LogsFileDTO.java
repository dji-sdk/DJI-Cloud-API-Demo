package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogsFileDTO {

    private String fileId;

    private String name;

    private Long size;

    private String logsId;

    private String deviceSn;

    private String fingerprint;

    private String objectKey;

    private Boolean status;

}
