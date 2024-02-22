package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.ObstacleAvoidance;
import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class ObstacleAvoidanceSet extends BaseModel {

    @Valid
    @NotNull
    private ObstacleAvoidance obstacleAvoidance;

    public ObstacleAvoidanceSet() {
    }

    @Override
    public String toString() {
        return "ObstacleAvoidanceSet{" +
                "obstacleAvoidance=" + obstacleAvoidance +
                '}';
    }

    public ObstacleAvoidance getObstacleAvoidance() {
        return obstacleAvoidance;
    }

    public ObstacleAvoidanceSet setObstacleAvoidance(ObstacleAvoidance obstacleAvoidance) {
        this.obstacleAvoidance = obstacleAvoidance;
        return this;
    }
}
