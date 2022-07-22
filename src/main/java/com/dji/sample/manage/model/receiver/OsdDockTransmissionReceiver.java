package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/17
 */
@Data
public class OsdDockTransmissionReceiver {

    private Integer flighttaskStepCode;

    private DockMediaFileDetailReceiver mediaFileDetail;

    private DockSdrReceiver sdr;
}
