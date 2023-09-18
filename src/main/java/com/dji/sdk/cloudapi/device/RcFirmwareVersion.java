package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/28
 */
public class RcFirmwareVersion {

    private String firmwareVersion;

    public RcFirmwareVersion() {
    }

    @Override
    public String toString() {
        return "RcFirmwareVersion{" +
                "firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public RcFirmwareVersion setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }

}
