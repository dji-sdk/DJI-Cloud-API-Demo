package com.dji.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2022/1/27
 */
public class DroneBattery {

    private List<Battery> batteries;

    private Integer capacityPercent;

    private Integer landingPower;

    private Integer remainFlightTime;

    private Integer returnHomePower;

    public DroneBattery() {
    }

    @Override
    public String toString() {
        return "DroneBattery{" +
                "batteries=" + batteries +
                ", capacityPercent=" + capacityPercent +
                ", landingPower=" + landingPower +
                ", remainFlightTime=" + remainFlightTime +
                ", returnHomePower=" + returnHomePower +
                '}';
    }

    public List<Battery> getBatteries() {
        return batteries;
    }

    public DroneBattery setBatteries(List<Battery> batteries) {
        this.batteries = batteries;
        return this;
    }

    public Integer getCapacityPercent() {
        return capacityPercent;
    }

    public DroneBattery setCapacityPercent(Integer capacityPercent) {
        this.capacityPercent = capacityPercent;
        return this;
    }

    public Integer getLandingPower() {
        return landingPower;
    }

    public DroneBattery setLandingPower(Integer landingPower) {
        this.landingPower = landingPower;
        return this;
    }

    public Integer getRemainFlightTime() {
        return remainFlightTime;
    }

    public DroneBattery setRemainFlightTime(Integer remainFlightTime) {
        this.remainFlightTime = remainFlightTime;
        return this;
    }

    public Integer getReturnHomePower() {
        return returnHomePower;
    }

    public DroneBattery setReturnHomePower(Integer returnHomePower) {
        this.returnHomePower = returnHomePower;
        return this;
    }
}
