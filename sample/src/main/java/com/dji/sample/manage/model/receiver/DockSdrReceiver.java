package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/17
 */
@Data
public class DockSdrReceiver {

    private Integer downQuality;

    private Double frequencyBand;

    private Integer upQuality;
}
