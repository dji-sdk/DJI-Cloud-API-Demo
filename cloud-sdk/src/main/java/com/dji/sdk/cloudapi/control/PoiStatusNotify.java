package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.wayline.FlighttaskStatusEnum;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class PoiStatusNotify {

    private FlighttaskStatusEnum status;

    private PoiStatusReasonEnum reason;

    private Float circleRadius;

    private Float circleSpeed;

    private Float maxCircleSpeed;

    public PoiStatusNotify() {
    }

    @Override
    public String toString() {
        return "PoiStatusNotify{" +
                "status=" + status +
                ", reason=" + reason +
                ", circleRadius=" + circleRadius +
                ", circleSpeed=" + circleSpeed +
                ", maxCircleSpeed=" + maxCircleSpeed +
                '}';
    }

    public FlighttaskStatusEnum getStatus() {
        return status;
    }

    public PoiStatusNotify setStatus(FlighttaskStatusEnum status) {
        this.status = status;
        return this;
    }

    public PoiStatusReasonEnum getReason() {
        return reason;
    }

    public PoiStatusNotify setReason(PoiStatusReasonEnum reason) {
        this.reason = reason;
        return this;
    }

    public Float getCircleRadius() {
        return circleRadius;
    }

    public PoiStatusNotify setCircleRadius(Float circleRadius) {
        this.circleRadius = circleRadius;
        return this;
    }

    public Float getCircleSpeed() {
        return circleSpeed;
    }

    public PoiStatusNotify setCircleSpeed(Float circleSpeed) {
        this.circleSpeed = circleSpeed;
        return this;
    }

    public Float getMaxCircleSpeed() {
        return maxCircleSpeed;
    }

    public PoiStatusNotify setMaxCircleSpeed(Float maxCircleSpeed) {
        this.maxCircleSpeed = maxCircleSpeed;
        return this;
    }
}
