package com.dji.sample.component.mqtt.model;

import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
@Data
public class OutputProgressReceiver {

    private Integer percent;

    private String stepKey;

    private Integer stepResult;
}
