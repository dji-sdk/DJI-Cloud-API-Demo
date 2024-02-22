package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class DockDroneWpmzVersion {

    private String wpmzVersion;

    public DockDroneWpmzVersion() {
    }

    @Override
    public String toString() {
        return "DockDroneWpmzVersion{" +
                "wpmzVersion='" + wpmzVersion + '\'' +
                '}';
    }

    public String getWpmzVersion() {
        return wpmzVersion;
    }

    public DockDroneWpmzVersion setWpmzVersion(String wpmzVersion) {
        this.wpmzVersion = wpmzVersion;
        return this;
    }
}
