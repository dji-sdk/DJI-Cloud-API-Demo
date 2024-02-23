package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
public class BackupBattery {

    private Integer voltage;

    private Float temperature;

    @JsonProperty("switch")
    private Boolean batterySwitch;

    public BackupBattery() {
    }

    @Override
    public String toString() {
        return "BackupBattery{" +
                "voltage=" + voltage +
                ", temperature=" + temperature +
                ", batterySwitch=" + batterySwitch +
                '}';
    }

    public Integer getVoltage() {
        return voltage;
    }

    public BackupBattery setVoltage(Integer voltage) {
        this.voltage = voltage;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public BackupBattery setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Boolean getBatterySwitch() {
        return batterySwitch;
    }

    public BackupBattery setBatterySwitch(Boolean batterySwitch) {
        this.batterySwitch = batterySwitch;
        return this;
    }
}
