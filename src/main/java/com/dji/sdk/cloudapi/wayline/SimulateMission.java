package com.dji.sdk.cloudapi.wayline;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/8/4
 */
public class SimulateMission {

    @NotNull
    private SimulateSwitchEnum isEnable;

    @NotNull
    @Min(-90)
    @Max(90)
    private Float latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Float longitude;

    public SimulateMission() {
    }

    @Override
    public String toString() {
        return "SimulateMission{" +
                "isEnable=" + isEnable +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public SimulateSwitchEnum getIsEnable() {
        return isEnable;
    }

    public SimulateMission setIsEnable(SimulateSwitchEnum isEnable) {
        this.isEnable = isEnable;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public SimulateMission setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public SimulateMission setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }
}
