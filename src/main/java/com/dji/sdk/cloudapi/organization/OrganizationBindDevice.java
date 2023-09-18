package com.dji.sdk.cloudapi.organization;

import com.dji.sdk.cloudapi.device.DeviceEnum;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/13
 */
public class OrganizationBindDevice {

    private String deviceBindingCode;

    private String organizationId;

    private String deviceCallsign;

    private String sn;

    private DeviceEnum deviceModelKey;

    public OrganizationBindDevice() {
    }

    @Override
    public String toString() {
        return "OrganizationBindDevice{" +
                "deviceBindingCode='" + deviceBindingCode + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", deviceCallsign='" + deviceCallsign + '\'' +
                ", sn='" + sn + '\'' +
                ", deviceModelKey=" + deviceModelKey +
                '}';
    }

    public String getDeviceBindingCode() {
        return deviceBindingCode;
    }

    public OrganizationBindDevice setDeviceBindingCode(String deviceBindingCode) {
        this.deviceBindingCode = deviceBindingCode;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public OrganizationBindDevice setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getDeviceCallsign() {
        return deviceCallsign;
    }

    public OrganizationBindDevice setDeviceCallsign(String deviceCallsign) {
        this.deviceCallsign = deviceCallsign;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public OrganizationBindDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public DeviceEnum getDeviceModelKey() {
        return deviceModelKey;
    }

    public OrganizationBindDevice setDeviceModelKey(DeviceEnum deviceModelKey) {
        this.deviceModelKey = deviceModelKey;
        return this;
    }
}
