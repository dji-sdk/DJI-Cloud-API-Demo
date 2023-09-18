package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class DroneBatteryMaintenance {

    private BatteryIndexEnum index;

    private Integer capacityPercent;

    private Integer voltage;

    private Float temperature;

    public DroneBatteryMaintenance() {
    }

    @Override
    public String toString() {
        return "DroneBatteryMaintenance{" +
                "index=" + index +
                ", capacityPercent=" + capacityPercent +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                '}';
    }

    public BatteryIndexEnum getIndex() {
        return index;
    }

    public DroneBatteryMaintenance setIndex(BatteryIndexEnum index) {
        this.index = index;
        return this;
    }

    public Integer getCapacityPercent() {
        return capacityPercent;
    }

    public DroneBatteryMaintenance setCapacityPercent(Integer capacityPercent) {
        this.capacityPercent = capacityPercent;
        return this;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public DroneBatteryMaintenance setVoltage(Integer voltage) {
        this.voltage = voltage;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public DroneBatteryMaintenance setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }
}
