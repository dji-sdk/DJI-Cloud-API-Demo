package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/8
 */
public class LiveviewWorldRegion {

    private Float bottom;

    private Float left;

    private Float right;

    private Float top;

    public LiveviewWorldRegion() {
    }

    @Override
    public String toString() {
        return "LiveviewWorldRegion{" +
                "bottom=" + bottom +
                ", left=" + left +
                ", right=" + right +
                ", top=" + top +
                '}';
    }

    public Float getBottom() {
        return bottom;
    }

    public LiveviewWorldRegion setBottom(Float bottom) {
        this.bottom = bottom;
        return this;
    }

    public Float getLeft() {
        return left;
    }

    public LiveviewWorldRegion setLeft(Float left) {
        this.left = left;
        return this;
    }

    public Float getRight() {
        return right;
    }

    public LiveviewWorldRegion setRight(Float right) {
        this.right = right;
        return this;
    }

    public Float getTop() {
        return top;
    }

    public LiveviewWorldRegion setTop(Float top) {
        this.top = top;
        return this;
    }
}
