package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
public class WirelessLinkStateReceiver {

    private Integer downloadQuality;

    private Double frequencyBand;

    private Integer upwardQuality;

}