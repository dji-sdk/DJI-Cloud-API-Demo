package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class FlyToPointUpdateRequest extends BaseModel {

    @Min(1)
    @Max(15)
    @NotNull
    private Integer maxSpeed;

    /**
     * The M30 series only support one point.
     */
    @Size(min = 1, max = 1)
    @NotNull
    private List<@Valid Point> points;

    public FlyToPointUpdateRequest() {
    }

    @Override
    public String toString() {
        return "FlyToPointUpdateRequest{" +
                "maxSpeed=" + maxSpeed +
                ", points=" + points +
                '}';
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public FlyToPointUpdateRequest setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public List<Point> getPoints() {
        return points;
    }

    public FlyToPointUpdateRequest setPoints(List<Point> points) {
        this.points = points;
        return this;
    }
}
