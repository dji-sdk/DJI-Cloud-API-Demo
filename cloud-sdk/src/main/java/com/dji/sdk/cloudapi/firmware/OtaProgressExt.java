package com.dji.sdk.cloudapi.firmware;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/30
 */
public class OtaProgressExt {

    private Long rate;

    public OtaProgressExt() {
    }

    @Override
    public String toString() {
        return "OtaProgressExt{" +
                "rate=" + rate +
                '}';
    }

    public Long getRate() {
        return rate;
    }

    public OtaProgressExt setRate(Long rate) {
        this.rate = rate;
        return this;
    }
}
