package com.dji.sdk.cloudapi.device;

import com.dji.sdk.cloudapi.control.CommanderFlightModeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class DockDroneCurrentCommanderFlightMode {

    @JsonProperty("current_commander_flight_mode")
    private CommanderFlightModeEnum currentCommanderFlightMode;

    public DockDroneCurrentCommanderFlightMode() {
    }

    @Override
    public String toString() {
        return "DockDroneCurrentCommanderFlightMode{" +
                "currentCommanderFlightMode=" + currentCommanderFlightMode +
                '}';
    }

    public CommanderFlightModeEnum getCurrentCommanderFlightMode() {
        return currentCommanderFlightMode;
    }

    public DockDroneCurrentCommanderFlightMode setCurrentCommanderFlightMode(CommanderFlightModeEnum currentCommanderFlightMode) {
        this.currentCommanderFlightMode = currentCommanderFlightMode;
        return this;
    }
}
