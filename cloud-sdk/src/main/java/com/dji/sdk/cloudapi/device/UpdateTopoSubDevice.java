package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/26
 */
public class UpdateTopoSubDevice {

    private String sn;

    private DeviceDomainEnum domain;

    private DeviceTypeEnum type;

    private DeviceSubTypeEnum subType;

    private ControlSourceEnum index;

    private String deviceSecret;

    private String nonce;

    private String thingVersion;

    public UpdateTopoSubDevice() {
    }

    @Override
    public String toString() {
        return "UpdateTopoSubDevice{" +
                "sn='" + sn + '\'' +
                ", domain=" + domain +
                ", type=" + type +
                ", subType=" + subType +
                ", index=" + index +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", nonce='" + nonce + '\'' +
                ", thingVersion=" + thingVersion +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public UpdateTopoSubDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public DeviceDomainEnum getDomain() {
        return domain;
    }

    public UpdateTopoSubDevice setDomain(DeviceDomainEnum domain) {
        this.domain = domain;
        return this;
    }

    public DeviceTypeEnum getType() {
        return type;
    }

    public UpdateTopoSubDevice setType(DeviceTypeEnum type) {
        this.type = type;
        return this;
    }

    public DeviceSubTypeEnum getSubType() {
        return subType;
    }

    public UpdateTopoSubDevice setSubType(DeviceSubTypeEnum subType) {
        this.subType = subType;
        return this;
    }

    public ControlSourceEnum getIndex() {
        return index;
    }

    public UpdateTopoSubDevice setIndex(ControlSourceEnum index) {
        this.index = index;
        return this;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public UpdateTopoSubDevice setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public UpdateTopoSubDevice setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getThingVersion() {
        return thingVersion;
    }

    public UpdateTopoSubDevice setThingVersion(String thingVersion) {
        this.thingVersion = thingVersion;
        return this;
    }
}
