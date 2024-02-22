package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/14
 */
public class FlyToPointRequest extends BaseModel {

    @Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")
    @NotNull
    private String flyToId;

    @Min(1)
    @Max(15)
    @NotNull
    private Integer maxSpeed;

    /**
     * The M30 series only support one point.
     */
    @Size(min = 1)
    @NotNull
    private List<@Valid Point> points;

    public FlyToPointRequest() {
    }

    @Override
    public String toString() {
        return "FlyToPointRequest{" +
                "flyToId='" + flyToId + '\'' +
                ", maxSpeed=" + maxSpeed +
                ", points=" + points +
                '}';
    }

    public String getFlyToId() {
        return flyToId;
    }

    public FlyToPointRequest setFlyToId(String flyToId) {
        this.flyToId = flyToId;
        return this;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public FlyToPointRequest setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public List<Point> getPoints() {
        return points;
    }

    public FlyToPointRequest setPoints(List<Point> points) {
        this.points = points;
        return this;
    }
}
