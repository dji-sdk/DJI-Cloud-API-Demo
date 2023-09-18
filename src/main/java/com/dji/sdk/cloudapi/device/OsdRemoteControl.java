package com.dji.sdk.cloudapi.device;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class OsdRemoteControl {

    private Float latitude;

    private Float longitude;

    private Float height;

    private Integer capacityPercent;

    private WirelessLink wirelessLink;

    public OsdRemoteControl() {
    }

    @Override
    public String toString() {
        return "OsdRemoteControl{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", capacityPercent=" + capacityPercent +
                ", wirelessLink=" + wirelessLink +
                '}';
    }

    public Float getLatitude() {
        return latitude;
    }

    public OsdRemoteControl setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public OsdRemoteControl setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public OsdRemoteControl setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Integer getCapacityPercent() {
        return capacityPercent;
    }

    public OsdRemoteControl setCapacityPercent(Integer capacityPercent) {
        this.capacityPercent = capacityPercent;
        return this;
    }

    public WirelessLink getWirelessLink() {
        return wirelessLink;
    }

    public OsdRemoteControl setWirelessLink(WirelessLink wirelessLink) {
        this.wirelessLink = wirelessLink;
        return this;
    }
}