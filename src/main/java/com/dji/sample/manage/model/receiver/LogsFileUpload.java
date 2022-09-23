package com.dji.sample.manage.model.receiver;

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
public class LogsFileUpload {

    private String deviceSn;

    private List<LogsFile> list;

    @JsonProperty("module")
    private String deviceModelDomain;

    private String objectKey;

    private Integer result;

    private String fileId;
}
