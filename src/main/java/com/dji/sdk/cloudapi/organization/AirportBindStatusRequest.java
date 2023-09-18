package com.dji.sdk.cloudapi.organization;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AirportBindStatusRequest {

    private List<BindStatusResponseDevice> devices;

    public AirportBindStatusRequest() {
    }

    @Override
    public String toString() {
        return "AirportBindStatusRequest{" +
                "devices=" + devices +
                '}';
    }

    public List<BindStatusResponseDevice> getDevices() {
        return devices;
    }

    public AirportBindStatusRequest setDevices(List<BindStatusResponseDevice> devices) {
        this.devices = devices;
        return this;
    }
}
