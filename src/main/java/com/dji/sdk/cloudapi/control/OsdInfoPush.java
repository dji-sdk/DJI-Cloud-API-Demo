package com.dji.sdk.cloudapi.control;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class OsdInfoPush {

    private Float attitudeHead;

    private Float latitude;

    private Float longitude;

    private Float height;

    private Float speedX;

    private Float speedY;

    private Float speedZ;

    private Float gimbalPitch;

    private Float gimbalRoll;

    private Float gimbalYaw;

    public OsdInfoPush() {
    }

    @Override
    public String toString() {
        return "OsdInfoPush{" +
                "attitudeHead=" + attitudeHead +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", speedX=" + speedX +
                ", speedY=" + speedY +
                ", speedZ=" + speedZ +
                ", gimbalPitch=" + gimbalPitch +
                ", gimbalRoll=" + gimbalRoll +
                ", gimbalYaw=" + gimbalYaw +
                '}';
    }

    public Float getAttitudeHead() {
        return attitudeHead;
    }

    public OsdInfoPush setAttitudeHead(Float attitudeHead) {
        this.attitudeHead = attitudeHead;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public OsdInfoPush setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public OsdInfoPush setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public OsdInfoPush setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Float getSpeedX() {
        return speedX;
    }

    public OsdInfoPush setSpeedX(Float speedX) {
        this.speedX = speedX;
        return this;
    }

    public Float getSpeedY() {
        return speedY;
    }

    public OsdInfoPush setSpeedY(Float speedY) {
        this.speedY = speedY;
        return this;
    }

    public Float getSpeedZ() {
        return speedZ;
    }

    public OsdInfoPush setSpeedZ(Float speedZ) {
        this.speedZ = speedZ;
        return this;
    }

    public Float getGimbalPitch() {
        return gimbalPitch;
    }

    public OsdInfoPush setGimbalPitch(Float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
        return this;
    }

    public Float getGimbalRoll() {
        return gimbalRoll;
    }

    public OsdInfoPush setGimbalRoll(Float gimbalRoll) {
        this.gimbalRoll = gimbalRoll;
        return this;
    }

    public Float getGimbalYaw() {
        return gimbalYaw;
    }

    public OsdInfoPush setGimbalYaw(Float gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
        return this;
    }
}
