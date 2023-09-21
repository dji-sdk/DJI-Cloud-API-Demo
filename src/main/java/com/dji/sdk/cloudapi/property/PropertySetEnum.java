package com.dji.sdk.cloudapi.property;

import com.dji.sdk.common.BaseModel;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public enum PropertySetEnum {

    NIGHT_LIGHTS_STATE("night_lights_state", NightLightsStateSet.class),

    HEIGHT_LIMIT("height_limit", HeightLimitSet.class),

    DISTANCE_LIMIT_STATUS("distance_limit_status", DistanceLimitStatusSet.class),

    OBSTACLE_AVOIDANCE("obstacle_avoidance", ObstacleAvoidanceSet.class),

    RTH_ALTITUDE("rth_altitude", RthAltitudeSet.class),

    OUT_OF_CONTROL_ACTION("rc_lost_action", RcLostActionSet.class),

    EXIT_WAYLINE_WHEN_RC_LOST("exit_wayline_when_rc_lost", ExitWaylineWhenRcLostSet.class),

    THERMAL_CURRENT_PALETTE_STYLE("thermal_current_palette_style", ThermalCurrentPaletteStyleSet.class),

    THERMAL_GAIN_MODE("thermal_gain_mode", ThermalGainModeSet.class),

    THERMAL_ISOTHERM_STATE("thermal_isotherm_state", ThermalIsothermStateSet.class),

    THERMAL_ISOTHERM_UPPER_LIMIT("thermal_isotherm_upper_limit", ThermalIsothermUpperLimitSet.class),

    THERMAL_ISOTHERM_LOWER_LIMIT("thermal_isotherm_lower_limit", ThermalIsothermLowerLimitSet.class),

    ;

    private final String property;

    private final Class<? extends BaseModel> clazz;

    PropertySetEnum(String property, Class<? extends BaseModel> clazz) {
        this.property = property;
        this.clazz = clazz;
    }

    public String getProperty() {
        return property;
    }

    public Class<? extends BaseModel> getClazz() {
        return clazz;
    }

    public static PropertySetEnum find(String property) {
        return Arrays.stream(values()).filter(propertyEnum -> propertyEnum.property.equals(property)).findAny()
                .orElseThrow(() -> new CloudSDKException(PropertySetEnum.class, property));
    }
}
