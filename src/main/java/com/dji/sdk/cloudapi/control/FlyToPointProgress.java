package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.wayline.WaylineErrorCodeEnum;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public class FlyToPointProgress {

    private WaylineErrorCodeEnum result;

    private FlyToStatusEnum status;

    private String flyToId;

    private Integer wayPointIndex;

    public FlyToPointProgress() {
    }

    @Override
    public String toString() {
        return "FlyToPointProgress{" +
                "result=" + result +
                ", status=" + status +
                ", flyToId='" + flyToId + '\'' +
                ", wayPointIndex=" + wayPointIndex +
                '}';
    }

    public WaylineErrorCodeEnum getResult() {
        return result;
    }

    public FlyToPointProgress setResult(WaylineErrorCodeEnum result) {
        this.result = result;
        return this;
    }

    public FlyToStatusEnum getStatus() {
        return status;
    }

    public FlyToPointProgress setStatus(FlyToStatusEnum status) {
        this.status = status;
        return this;
    }

    public String getFlyToId() {
        return flyToId;
    }

    public FlyToPointProgress setFlyToId(String flyToId) {
        this.flyToId = flyToId;
        return this;
    }

    public Integer getWayPointIndex() {
        return wayPointIndex;
    }

    public FlyToPointProgress setWayPointIndex(Integer wayPointIndex) {
        this.wayPointIndex = wayPointIndex;
        return this;
    }
}
