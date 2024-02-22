package com.dji.sdk.cloudapi.map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapFile {

    /**
     * The offline map file name will be used as a way to determine the version, and the format is: offline_map_{sync_method}_{version}.
     * offline_map: is a fixed prefix, sync_method: data synchronization method - full (full), version: version number
     */
    @NotNull
    @Pattern(regexp = "^offline_map_full_\\w+\\.rocksdb\\.zip$")
    private String name;

    @NotNull
    private String url;

    /**
     * Calculated using SHA256, this value can be used to confirm whether the file is complete.
     */
    @NotNull
    private String checksum;

    /**
     * The size of this file in bytes.
     */
    @NotNull
    private Long size;

    public OfflineMapFile() {
    }

    @Override
    public String toString() {
        return "OfflineMapFile{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", checksum='" + checksum + '\'' +
                ", size=" + size +
                '}';
    }

    public String getName() {
        return name;
    }

    public OfflineMapFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public OfflineMapFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public OfflineMapFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public OfflineMapFile setSize(Long size) {
        this.size = size;
        return this;
    }
}
