package com.dji.sample.manage.model.enums;

import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import com.dji.sample.manage.model.receiver.DistanceLimitStatusReceiver;
import com.dji.sample.manage.model.receiver.HeightLimitReceiver;
import com.dji.sample.manage.model.receiver.ObstacleAvoidanceReceiver;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@Getter
public enum DeviceSetPropertyEnum {

    NIGHT_LIGHTS_STATE("night_lights_state", StateSwitchReceiver.class),

    HEIGHT_LIMIT("height_limit", HeightLimitReceiver.class),

    DISTANCE_LIMIT_STATUS("distance_limit_status", DistanceLimitStatusReceiver.class),

    OBSTACLE_AVOIDANCE("obstacle_avoidance", ObstacleAvoidanceReceiver.class);


    String property;

    Class<? extends BasicDeviceProperty> clazz;

    DeviceSetPropertyEnum(String property, Class<? extends BasicDeviceProperty> clazz) {
        this.property = property;
        this.clazz = clazz;
    }

    public static Optional<DeviceSetPropertyEnum> find(String property) {
        return Arrays.stream(DeviceSetPropertyEnum.values()).filter(propertyEnum -> propertyEnum.property.equals(property)).findAny();
    }
}
