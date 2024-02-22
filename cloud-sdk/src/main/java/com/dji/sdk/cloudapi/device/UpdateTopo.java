package com.dji.sdk.cloudapi.device;


import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/26
 */
public class UpdateTopo {

    private DeviceDomainEnum domain;

    private DeviceTypeEnum type;

    private DeviceSubTypeEnum subType;

    private String deviceSecret;

    private String nonce;

    private String thingVersion;

    private List<UpdateTopoSubDevice> subDevices;

    public UpdateTopo() {
    }

    @Override
    public String toString() {
        return "UpdateTopo{" +
                "domain=" + domain +
                ", type=" + type +
                ", subType=" + subType +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", nonce='" + nonce + '\'' +
                ", thingVersion=" + thingVersion +
                ", subDevices=" + subDevices +
                '}';
    }

    public DeviceDomainEnum getDomain() {
        return domain;
    }

    public UpdateTopo setDomain(DeviceDomainEnum domain) {
        this.domain = domain;
        return this;
    }

    public DeviceTypeEnum getType() {
        return type;
    }

    public UpdateTopo setType(DeviceTypeEnum type) {
        this.type = type;
        return this;
    }

    public DeviceSubTypeEnum getSubType() {
        return subType;
    }

    public UpdateTopo setSubType(DeviceSubTypeEnum subType) {
        this.subType = subType;
        return this;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public UpdateTopo setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public UpdateTopo setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getThingVersion() {
        return thingVersion;
    }

    public UpdateTopo setThingVersion(String thingVersion) {
        this.thingVersion = thingVersion;
        return this;
    }

    public List<UpdateTopoSubDevice> getSubDevices() {
        return subDevices;
    }

    public UpdateTopo setSubDevices(List<UpdateTopoSubDevice> subDevices) {
        this.subDevices = subDevices;
        return this;
    }
}
