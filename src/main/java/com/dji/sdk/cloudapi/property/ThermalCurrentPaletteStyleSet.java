package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.PayloadIndex;
import com.dji.sdk.cloudapi.device.ThermalPaletteStyleEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class ThermalCurrentPaletteStyleSet extends BaseModel {

    @NotNull
    @Valid
    private PayloadIndex payloadIndex;

    @NotNull
    private ThermalPaletteStyleEnum thermalCurrentPaletteStyle;

    public ThermalCurrentPaletteStyleSet() {
    }

    @Override
    public String toString() {
        return "ThermalCurrentPaletteStyleSet{" +
                "payloadIndex=" + payloadIndex +
                ", thermalCurrentPaletteStyle=" + thermalCurrentPaletteStyle +
                '}';
    }

    @JsonValue
    public Map<String, Object> toMap() {
        return Map.of(payloadIndex.toString(), Map.of("thermal_current_palette_style", thermalCurrentPaletteStyle.getStyle()));
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public ThermalCurrentPaletteStyleSet setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ThermalPaletteStyleEnum getThermalCurrentPaletteStyle() {
        return thermalCurrentPaletteStyle;
    }

    public ThermalCurrentPaletteStyleSet setThermalCurrentPaletteStyle(ThermalPaletteStyleEnum thermalCurrentPaletteStyle) {
        this.thermalCurrentPaletteStyle = thermalCurrentPaletteStyle;
        return this;
    }
}
