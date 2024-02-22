package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class IrMeteringArea {

    private Float x;

    private Float y;

    private Float temperature;

    private Float width;

    private Float height;

    private Float averTemperature;

    private IrMeteringPoint minTemperaturePoint;

    private IrMeteringPoint maxTemperaturePoint;

    public IrMeteringArea() {
    }

    @Override
    public String toString() {
        return "IrMeteringArea{" +
                "x=" + x +
                ", y=" + y +
                ", temperature=" + temperature +
                ", width=" + width +
                ", height=" + height +
                ", averTemperature=" + averTemperature +
                ", minTemperaturePoint=" + minTemperaturePoint +
                ", maxTemperaturePoint=" + maxTemperaturePoint +
                '}';
    }

    public Float getX() {
        return x;
    }

    public IrMeteringArea setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public IrMeteringArea setY(Float y) {
        this.y = y;
        return this;
    }

    public Float getTemperature() {
        return temperature;
    }

    public IrMeteringArea setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Float getWidth() {
        return width;
    }

    public IrMeteringArea setWidth(Float width) {
        this.width = width;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public IrMeteringArea setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Float getAverTemperature() {
        return averTemperature;
    }

    public IrMeteringArea setAverTemperature(Float averTemperature) {
        this.averTemperature = averTemperature;
        return this;
    }

    public IrMeteringPoint getMinTemperaturePoint() {
        return minTemperaturePoint;
    }

    public IrMeteringArea setMinTemperaturePoint(IrMeteringPoint minTemperaturePoint) {
        this.minTemperaturePoint = minTemperaturePoint;
        return this;
    }

    public IrMeteringPoint getMaxTemperaturePoint() {
        return maxTemperaturePoint;
    }

    public IrMeteringArea setMaxTemperaturePoint(IrMeteringPoint maxTemperaturePoint) {
        this.maxTemperaturePoint = maxTemperaturePoint;
        return this;
    }
}
