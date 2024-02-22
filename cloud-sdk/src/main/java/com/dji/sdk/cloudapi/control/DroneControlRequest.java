package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class DroneControlRequest extends BaseModel {

    @NotNull
    private Long seq;

    @Min(-17)
    @Max(17)
    private Float x;

    @Min(-17)
    @Max(17)
    private Float y;

    @Min(-4)
    @Max(5)
    private Float h;

    @Min(-90)
    @Max(90)
    private Float w;

    @Min(2)
    @Max(10)
    private Integer freq;

    @Min(100)
    @Max(1000)
    private Integer delayTime;

    public DroneControlRequest() {
    }

    @Override
    public String toString() {
        return "DroneControlRequest{" +
                "seq=" + seq +
                ", x=" + x +
                ", y=" + y +
                ", h=" + h +
                ", w=" + w +
                ", freq=" + freq +
                ", delayTime=" + delayTime +
                '}';
    }

    public Long getSeq() {
        return seq;
    }

    public DroneControlRequest setSeq(Long seq) {
        this.seq = seq;
        return this;
    }

    public Float getX() {
        return x;
    }

    public DroneControlRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public DroneControlRequest setY(Float y) {
        this.y = y;
        return this;
    }

    public Float getH() {
        return h;
    }

    public DroneControlRequest setH(Float h) {
        this.h = h;
        return this;
    }

    public Float getW() {
        return w;
    }

    public DroneControlRequest setW(Float w) {
        this.w = w;
        return this;
    }

    public Integer getFreq() {
        return freq;
    }

    public DroneControlRequest setFreq(Integer freq) {
        this.freq = freq;
        return this;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public DroneControlRequest setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
        return this;
    }
}
