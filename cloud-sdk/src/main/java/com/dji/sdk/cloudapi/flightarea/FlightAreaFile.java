package com.dji.sdk.cloudapi.flightarea;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreaFile {

    /**
     * Custom flight area file name
     */
    private String name;

    /**
     * File SHA256 signature
     */
    private String checksum;

    public FlightAreaFile() {
    }

    @Override
    public String toString() {
        return "FlightAreaFile{" +
                "name='" + name + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public FlightAreaFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public FlightAreaFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
}
