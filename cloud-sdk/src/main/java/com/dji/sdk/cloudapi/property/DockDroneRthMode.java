package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.wayline.RthModeEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public class DockDroneRthMode extends BaseModel {

    @JsonProperty("rth_mode")
    @NotNull
    private RthModeEnum rthMode;

    public DockDroneRthMode() {
    }

    @Override
    public String toString() {
        return "DockDroneRthMode{" +
                "rthMode=" + rthMode +
                '}';
    }

    public RthModeEnum getRthMode() {
        return rthMode;
    }

    public DockDroneRthMode setRthMode(RthModeEnum rthMode) {
        this.rthMode = rthMode;
        return this;
    }
}
