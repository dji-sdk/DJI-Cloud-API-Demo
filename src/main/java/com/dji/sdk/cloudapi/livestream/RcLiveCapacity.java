package com.dji.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class RcLiveCapacity {

    /**
     * Total number of video streams available for livestreaming.
     * Indicates the total number of all available live video streams owned by the aircraft or device.
     */
    private Integer availableVideoNumber;

    /**
     * Maximum total number of video streams that can be lived stream simultaneously.
     */
    private Integer coexistVideoNumberMax;

    /**
     * Device live streaming capability list
     */
    private List<RcLiveCapacityDevice> deviceList;

    public RcLiveCapacity() {
    }

    @Override
    public String toString() {
        return "RcLiveCapacity{" +
                "availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", deviceList=" + deviceList +
                '}';
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public RcLiveCapacity setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public RcLiveCapacity setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public List<RcLiveCapacityDevice> getDeviceList() {
        return deviceList;
    }

    public RcLiveCapacity setDeviceList(List<RcLiveCapacityDevice> deviceList) {
        this.deviceList = deviceList;
        return this;
    }
}
