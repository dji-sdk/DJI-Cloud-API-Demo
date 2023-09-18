package com.dji.sdk.cloudapi.organization;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AirportOrganizationBindRequest {

    private List<OrganizationBindDevice> bindDevices;

    public AirportOrganizationBindRequest() {
    }

    @Override
    public String toString() {
        return "AirportOrganizationBindRequest{" +
                "bindDevices=" + bindDevices +
                '}';
    }

    public List<OrganizationBindDevice> getBindDevices() {
        return bindDevices;
    }

    public AirportOrganizationBindRequest setBindDevices(List<OrganizationBindDevice> bindDevices) {
        this.bindDevices = bindDevices;
        return this;
    }
}
