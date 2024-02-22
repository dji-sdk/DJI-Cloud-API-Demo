package com.dji.sdk.cloudapi.organization;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class BindStatusResponseDevice {

    private String sn;

    public BindStatusResponseDevice() {
    }

    @Override
    public String toString() {
        return "BindStatusResponseDevice{" +
                "sn='" + sn + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public BindStatusResponseDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }
}
