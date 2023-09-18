package com.dji.sdk.cloudapi.device;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class DockPayload {

    private PayloadIndex payloadIndex;

    private ThermalPaletteStyleEnum[] thermalSupportedPaletteStyles;

    private Integer version;

    public DockPayload() {
    }

    @Override
    public String toString() {
        return "DockPayload{" +
                "payloadIndex=" + payloadIndex +
                ", thermalSupportedPaletteStyles=" + Arrays.toString(thermalSupportedPaletteStyles) +
                ", version=" + version +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public DockPayload setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ThermalPaletteStyleEnum[] getThermalSupportedPaletteStyles() {
        return thermalSupportedPaletteStyles;
    }

    public DockPayload setThermalSupportedPaletteStyles(ThermalPaletteStyleEnum[] thermalSupportedPaletteStyles) {
        this.thermalSupportedPaletteStyles = thermalSupportedPaletteStyles;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public DockPayload setVersion(Integer version) {
        this.version = version;
        return this;
    }
}
