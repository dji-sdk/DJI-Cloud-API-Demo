package com.dji.sdk.cloudapi.organization;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/13
 */
public class AirportOrganizationGetResponse extends BaseModel {

    @NotNull
    private String organizationName;

    public AirportOrganizationGetResponse() {
    }

    @Override
    public String toString() {
        return "AirportOrganizationGetResponse{" +
                "organizationName='" + organizationName + '\'' +
                '}';
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public AirportOrganizationGetResponse setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }
}
