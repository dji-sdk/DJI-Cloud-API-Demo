package com.dji.sample.manage.model.enums;

import com.dji.sample.manage.model.receiver.*;
import com.dji.sdk.cloudapi.property.PropertySetEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public enum PropertySetFieldEnum {

    NIGHT_LIGHTS_STATE(PropertySetEnum.NIGHT_LIGHTS_STATE, NightLightsStateReceiver.class),

    HEIGHT_LIMIT(PropertySetEnum.HEIGHT_LIMIT, HeightLimitReceiver.class),

    DISTANCE_LIMIT_STATUS(PropertySetEnum.DISTANCE_LIMIT_STATUS, DistanceLimitStatusReceiver.class),

    OBSTACLE_AVOIDANCE(PropertySetEnum.OBSTACLE_AVOIDANCE, ObstacleAvoidanceReceiver.class),

    RTH_ALTITUDE(PropertySetEnum.RTH_ALTITUDE, RthAltitudeReceiver.class),

    OUT_OF_CONTROL_ACTION(PropertySetEnum.OUT_OF_CONTROL_ACTION, OutOfControlActionReceiver.class),

//    EXIT_WAYLINE_WHEN_RC_LOST(PropertySetEnum.EXIT_WAYLINE_WHEN_RC_LOST, .class),
//
//    THERMAL_CURRENT_PALETTE_STYLE(PropertySetEnum.THERMAL_CURRENT_PALETTE_STYLE, .class),
//
//    THERMAL_GAIN_MODE(PropertySetEnum.THERMAL_GAIN_MODE, .class),
//
//    THERMAL_ISOTHERM_STATE(PropertySetEnum.THERMAL_ISOTHERM_STATE, .class),
//
//    THERMAL_ISOTHERM_UPPER_LIMIT(PropertySetEnum.THERMAL_ISOTHERM_UPPER_LIMIT, .class),
//
//    THERMAL_ISOTHERM_LOWER_LIMIT(PropertySetEnum.THERMAL_ISOTHERM_LOWER_LIMIT, .class),

    ;

    private final PropertySetEnum property;

    private final Class<? extends BasicDeviceProperty> clazz;

    PropertySetFieldEnum(PropertySetEnum property, Class<? extends BasicDeviceProperty> clazz) {
        this.property = property;
        this.clazz = clazz;
    }

    public PropertySetEnum getProperty() {
        return property;
    }

    @JsonValue
    public String getPropertyName() {
        return property.getProperty();
    }

    public Class<? extends BasicDeviceProperty> getClazz() {
        return clazz;
    }

    public static PropertySetFieldEnum find(String property) {
        return Arrays.stream(values()).filter(propertyEnum -> propertyEnum.property.getProperty().equals(property)).findAny()
                .orElseThrow();
    }
}
