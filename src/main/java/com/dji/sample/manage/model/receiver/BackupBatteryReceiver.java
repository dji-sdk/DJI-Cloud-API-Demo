package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
@Data
public class BackupBatteryReceiver {

    private Integer voltage;

    private Float temperature;

    @JsonProperty("switch")
    private Integer batterySwitch;
}
