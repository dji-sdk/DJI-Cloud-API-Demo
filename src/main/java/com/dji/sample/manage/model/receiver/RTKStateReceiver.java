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
public class RTKStateReceiver {

    private Integer bdsNumber;

    private Integer galNumber;

    private Integer gloNumber;

    private Integer gpsNumber;

    private Boolean isFixed;

    private Integer quality;

}