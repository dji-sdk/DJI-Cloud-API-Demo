package com.dji.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public class RcLiveCapacityDevice {

    /**
     * Device serial number
     */
    private String sn;

    /**
     * Total number of video streams that can be used for livestreaming
     * Total number of video streams used for livestreaming that belongs to devices.
     */
    private Integer availableVideoNumber;

    /**
     * Maximum number of video streams that can be used for livestreaming at the same time
     */
    private Integer coexistVideoNumberMax;

    /**
     * Camera list on the device
     */
    private List<RcLiveCapacityCamera> cameraList;

    public RcLiveCapacityDevice() {
    }

    @Override
    public String toString() {
        return "RcLiveCapacityDevice{" +
                "sn='" + sn + '\'' +
                ", availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", cameraList=" + cameraList +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public RcLiveCapacityDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public RcLiveCapacityDevice setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public RcLiveCapacityDevice setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public List<RcLiveCapacityCamera> getCameraList() {
        return cameraList;
    }

    public RcLiveCapacityDevice setCameraList(List<RcLiveCapacityCamera> cameraList) {
        this.cameraList = cameraList;
        return this;
    }
}