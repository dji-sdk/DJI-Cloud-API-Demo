package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.wayline.RthModeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public class DockDroneCurrentRthMode {

    /**
     * Current RTH height mode
     */
    @JsonProperty("current_rth_mode")
    @NotNull
    private RthModeEnum currentRthMode;

    public DockDroneCurrentRthMode() {
    }

    @Override
    public String toString() {
        return "DockDroneCurrentRthMode{" +
                "currentRthMode=" + currentRthMode +
                '}';
    }

    public RthModeEnum getCurrentRthMode() {
        return currentRthMode;
    }

    public DockDroneCurrentRthMode setCurrentRthMode(RthModeEnum currentRthMode) {
        this.currentRthMode = currentRthMode;
        return this;
    }
}
