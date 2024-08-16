package com.dji.sdk.cloudapi.property;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class DockDroneCommanderFlightHeight extends BaseModel {

    @JsonProperty("commander_flight_height")
    @NotNull
    @Min(2)
    @Max(3000)
    private Float commanderFlightHeight;

    public DockDroneCommanderFlightHeight() {
    }

    @Override
    public String toString() {
        return "DockDroneCommanderFlightHeight{" +
                "commanderFlightHeight=" + commanderFlightHeight +
                '}';
    }

    public Float getCommanderFlightHeight() {
        return commanderFlightHeight;
    }

    public DockDroneCommanderFlightHeight setCommanderFlightHeight(Float commanderFlightHeight) {
        this.commanderFlightHeight = commanderFlightHeight;
        return this;
    }
}
