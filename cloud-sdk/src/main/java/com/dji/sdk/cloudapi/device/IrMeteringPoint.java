package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class IrMeteringPoint {

    private Float x;

    private Float y;

    private Float temperature;

    public IrMeteringPoint() {
    }

    @Override
    public String toString() {
        return "IrMeteringPoint{" +
                "x=" + x +
                ", y=" + y +
                ", temperature=" + temperature +
                '}';
    }

    public Float getX() {
        return x;
    }

    public IrMeteringPoint setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public IrMeteringPoint setY(Float y) {
        this.y = y;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public IrMeteringPoint setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }
}
