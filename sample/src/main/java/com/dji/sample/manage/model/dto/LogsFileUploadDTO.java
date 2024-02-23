package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.log.LogFileIndex;
import com.dji.sdk.cloudapi.log.LogModuleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogsFileUploadDTO {

    private String deviceSn;

    private List<LogFileIndex> list;

    @JsonProperty("module")
    private LogModuleEnum deviceModelDomain;

    private String objectKey;

    private Integer result;

    private String fileId;
}
