package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
@Data
public class HmsArgsReceiver {

    private Long componentIndex;

    private Integer sensorIndex;

    private Integer alarmId;
}
