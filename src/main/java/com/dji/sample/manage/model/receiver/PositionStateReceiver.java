package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/27
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PositionStateReceiver {

    private Integer isCalibration;

    private Integer gpsNumber;

    private Integer isFixed;

    private Integer quality;

    private Integer rtkNumber;
}
