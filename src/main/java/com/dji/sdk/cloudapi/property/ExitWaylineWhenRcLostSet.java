package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.ExitWaylineWhenRcLostEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
public class ExitWaylineWhenRcLostSet extends BaseModel {

    @NotNull
    @JsonProperty("exit_wayline_when_rc_lost")
    private ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost;

    public ExitWaylineWhenRcLostSet() {
    }

    @Override
    public String toString() {
        return "ExitWaylineWhenRcLostSet{" +
                "exitWaylineWhenRcLost=" + exitWaylineWhenRcLost +
                '}';
    }

    public ExitWaylineWhenRcLostEnum getExitWaylineWhenRcLost() {
        return exitWaylineWhenRcLost;
    }

    public ExitWaylineWhenRcLostSet setExitWaylineWhenRcLost(ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost) {
        this.exitWaylineWhenRcLost = exitWaylineWhenRcLost;
        return this;
    }
}
