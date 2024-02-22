package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/28
 */
public class FirmwareVersion {

    private String firmwareVersion;

    public FirmwareVersion() {
    }

    @Override
    public String toString() {
        return "FirmwareVersion{" +
                "firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public FirmwareVersion setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }

}
