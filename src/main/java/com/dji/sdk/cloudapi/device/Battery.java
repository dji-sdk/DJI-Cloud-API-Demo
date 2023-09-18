package com.dji.sdk.cloudapi.device;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
public class Battery {

    private String firmwareVersion;

    private BatteryIndexEnum index;

    private Integer loopTimes;

    private Integer capacityPercent;

    private String sn;

    private Integer subType;

    private Float temperature;

    private Integer type;

    private Integer voltage;

    private Integer highVoltageStorageDays;

    public Battery() {
    }

    @Override
    public String toString() {
        return "Battery{" +
                "firmwareVersion='" + firmwareVersion + '\'' +
                ", index=" + index +
                ", loopTimes=" + loopTimes +
                ", capacityPercent=" + capacityPercent +
                ", sn='" + sn + '\'' +
                ", subType=" + subType +
                ", temperature=" + temperature +
                ", type=" + type +
                ", voltage=" + voltage +
                ", highVoltageStorageDays=" + highVoltageStorageDays +
                '}';
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public Battery setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }

    public BatteryIndexEnum getIndex() {
        return index;
    }

    public Battery setIndex(BatteryIndexEnum index) {
        this.index = index;
        return this;
    }

    public Integer getLoopTimes() {
        return loopTimes;
    }

    public Battery setLoopTimes(Integer loopTimes) {
        this.loopTimes = loopTimes;
        return this;
    }

    public Integer getCapacityPercent() {
        return capacityPercent;
    }

    public Battery setCapacityPercent(Integer capacityPercent) {
        this.capacityPercent = capacityPercent;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public Battery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public Integer getSubType() {
        return subType;
    }

    public Battery setSubType(Integer subType) {
        this.subType = subType;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Battery setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public Battery setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public Battery setVoltage(Integer voltage) {
        this.voltage = voltage;
        return this;
    }

    public Integer getHighVoltageStorageDays() {
        return highVoltageStorageDays;
    }

    public Battery setHighVoltageStorageDays(Integer highVoltageStorageDays) {
        this.highVoltageStorageDays = highVoltageStorageDays;
        return this;
    }
}