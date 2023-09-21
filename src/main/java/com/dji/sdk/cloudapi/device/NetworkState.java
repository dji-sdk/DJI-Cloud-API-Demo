package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/11
 */
public class NetworkState {

    private NetworkStateTypeEnum type;

    private NetworkStateQualityEnum quality;

    private Float rate;

    public NetworkState() {
    }

    @Override
    public String toString() {
        return "NetworkState{" +
                "type=" + type +
                ", quality=" + quality +
                ", rate=" + rate +
                '}';
    }

    public NetworkStateTypeEnum getType() {
        return type;
    }

    public NetworkState setType(NetworkStateTypeEnum type) {
        this.type = type;
        return this;
    }

    public NetworkStateQualityEnum getQuality() {
        return quality;
    }

    public NetworkState setQuality(NetworkStateQualityEnum quality) {
        this.quality = quality;
        return this;
    }

    public Float getRate() {
        return rate;
    }

    public NetworkState setRate(Float rate) {
        this.rate = rate;
        return this;
    }
}
