package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.property.SilentModeEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class DockSilentMode extends BaseModel {

    @NotNull
    @JsonProperty("silent_mode")
    private SilentModeEnum silentMode;

    public DockSilentMode() {
    }

    @Override
    public String toString() {
        return "DockSilentMode{" +
                "silentMode=" + silentMode +
                '}';
    }

    public SilentModeEnum getSilentMode() {
        return silentMode;
    }

    public DockSilentMode setSilentMode(SilentModeEnum silentMode) {
        this.silentMode = silentMode;
        return this;
    }
}
