package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AirConditioner {

    private AirConditionerStateEnum airConditionerState;

    private Integer switchTime;

    public AirConditioner() {
    }

    @Override
    public String toString() {
        return "AirConditioner{" +
                "airConditionerState=" + airConditionerState +
                ", switchTime=" + switchTime +
                '}';
    }

    public AirConditionerStateEnum getAirConditionerState() {
        return airConditionerState;
    }

    public AirConditioner setAirConditionerState(AirConditionerStateEnum airConditionerState) {
        this.airConditionerState = airConditionerState;
        return this;
    }

    public Integer getSwitchTime() {
        return switchTime;
    }

    public AirConditioner setSwitchTime(Integer switchTime) {
        this.switchTime = switchTime;
        return this;
    }
}
