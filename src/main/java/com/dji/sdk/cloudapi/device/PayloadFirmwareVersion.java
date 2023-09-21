package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/28
 */
public class PayloadFirmwareVersion {

    private PayloadPositionEnum position;

    private String firmwareVersion;

    public PayloadFirmwareVersion() {
    }

    @JsonCreator
    public PayloadFirmwareVersion(Map<String, Object> map) {
        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) map.entrySet().toArray()[0];
        this.position = PayloadPositionEnum.find(Integer.parseInt(entry.getKey().split("-")[1]));
        this.firmwareVersion = ((Map<String, String>) entry.getValue()).values().toArray(String[]::new)[0];
    }

    @Override
    public String toString() {
        return "PayloadFirmwareVersion{" +
                "position=" + position +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }

    public PayloadPositionEnum getPosition() {
        return position;
    }

    public PayloadFirmwareVersion setPosition(PayloadPositionEnum position) {
        this.position = position;
        return this;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public PayloadFirmwareVersion setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }
}
