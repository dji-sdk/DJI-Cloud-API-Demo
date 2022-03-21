package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BatteryStateReceiver {

    private String firmwareVersion;

    private Integer index;

    private Integer loopTimes;

    private Integer capacityPercent;

    private String sn;

    private Integer subType;

    private Float temperature;

    private Integer type;

    private Integer voltage;

}