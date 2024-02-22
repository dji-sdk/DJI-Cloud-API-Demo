package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/12
 */
public class DrcModeEnterRequest extends BaseModel {

    @NotNull
    @Valid
    private DrcModeMqttBroker mqttBroker;

    /**
     * range: 1 - 30
     */
    @Min(1)
    @Max(30)
    @NotNull
    private Integer osdFrequency;

    /**
     * range: 1 - 30
     */
    @Min(1)
    @Max(30)
    @NotNull
    private Integer hsiFrequency;

    public DrcModeEnterRequest() {
    }

    @Override
    public String toString() {
        return "DrcModeEnterRequest{" +
                "mqttBroker=" + mqttBroker +
                ", osdFrequency=" + osdFrequency +
                ", hsiFrequency=" + hsiFrequency +
                '}';
    }

    public DrcModeMqttBroker getMqttBroker() {
        return mqttBroker;
    }

    public DrcModeEnterRequest setMqttBroker(DrcModeMqttBroker mqttBroker) {
        this.mqttBroker = mqttBroker;
        return this;
    }

    public Integer getOsdFrequency() {
        return osdFrequency;
    }

    public DrcModeEnterRequest setOsdFrequency(Integer osdFrequency) {
        this.osdFrequency = osdFrequency;
        return this;
    }

    public Integer getHsiFrequency() {
        return hsiFrequency;
    }

    public DrcModeEnterRequest setHsiFrequency(Integer hsiFrequency) {
        this.hsiFrequency = hsiFrequency;
        return this;
    }
}
