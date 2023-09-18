package com.dji.sdk.cloudapi.organization;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/14
 */
public class BindStatusRequestDevice {

    @NotNull
    private String sn;

    @NotNull
    @JsonProperty("is_device_bind_organization")
    private Boolean deviceBindOrganization;

    @NotNull
    private String organizationId;

    @NotNull
    private String organizationName;

    @NotNull
    private String deviceCallsign;

    public BindStatusRequestDevice() {
    }

    @Override
    public String toString() {
        return "BindStatusRequestDevice{" +
                "sn='" + sn + '\'' +
                ", deviceBindOrganization=" + deviceBindOrganization +
                ", organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", deviceCallsign='" + deviceCallsign + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public BindStatusRequestDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public Boolean getDeviceBindOrganization() {
        return deviceBindOrganization;
    }

    public BindStatusRequestDevice setDeviceBindOrganization(Boolean deviceBindOrganization) {
        this.deviceBindOrganization = deviceBindOrganization;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public BindStatusRequestDevice setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public BindStatusRequestDevice setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public String getDeviceCallsign() {
        return deviceCallsign;
    }

    public BindStatusRequestDevice setDeviceCallsign(String deviceCallsign) {
        this.deviceCallsign = deviceCallsign;
        return this;
    }
}
