package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class ReadyConditions extends BaseModel {

    /**
     * Battery capacity
     * The aircraft battery percentage threshold of the executable task.
     * The aircraft battery must be greater than the `battery_capacity` when the task starts.
     */
    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    /**
     * Start time of the task executable period
     * Start millisecond timestamp of the task executable period. The task execution time should be later than the `begin_time`.
     */
    @NotNull
    private Long beginTime;

    /**
     * End time of the task executable period
     * End millisecond timestamp of the task executable period. The task execution time should be earlier than the `end_time`.
     */
    @NotNull
    private Long endTime;

    public ReadyConditions() {}

    @Override
    public String toString() {
        return "ReadyConditions{" +
                "batteryCapacity=" + batteryCapacity +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public ReadyConditions setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
        return this;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public ReadyConditions setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public ReadyConditions setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }
}