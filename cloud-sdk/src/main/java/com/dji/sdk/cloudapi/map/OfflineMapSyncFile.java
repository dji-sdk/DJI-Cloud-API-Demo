package com.dji.sdk.cloudapi.map;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapSyncFile {

    /**
     * The offline map file name will be used as a way to determine the version, and the format is: offline_map_{sync_method}_{version}.
     * offline_map: is a fixed prefix, sync_method: data synchronization method - full (full), version: version number
     */
    private String name;

    /**
     * Calculated using SHA256, this value can be used to confirm whether the file is complete.
     */
    private String checksum;

    public OfflineMapSyncFile() {
    }

    @Override
    public String toString() {
        return "OfflineMapSyncFile{" +
                "name='" + name + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public OfflineMapSyncFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public OfflineMapSyncFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
}
