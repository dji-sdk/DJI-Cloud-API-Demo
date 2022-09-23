package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class LogsExtFileReceiver {

    @JsonProperty("module")
    private String deviceModelDomain;

    private Long size;

    private String deviceSn;

    private String key;

    private String fingerprint;

    private LogsProgressReceiver progress;

}
