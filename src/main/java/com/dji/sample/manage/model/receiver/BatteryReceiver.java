package com.dji.sample.manage.model.receiver;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/27
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BatteryReceiver {

    private List<BatteryStateReceiver> batteries;

    private Integer capacityPercent;

    private Integer landingPower;

    private Integer remainFlightTime;

    private Integer returnHomePower;
}
