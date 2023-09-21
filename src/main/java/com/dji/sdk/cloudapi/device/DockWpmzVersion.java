package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class DockWpmzVersion {

    private String wpmzVersion;

    public DockWpmzVersion() {
    }

    @Override
    public String toString() {
        return "DockWpmzVersion{" +
                "wpmzVersion='" + wpmzVersion + '\'' +
                '}';
    }

    public String getWpmzVersion() {
        return wpmzVersion;
    }

    public DockWpmzVersion setWpmzVersion(String wpmzVersion) {
        this.wpmzVersion = wpmzVersion;
        return this;
    }
}
