package com.dji.sdk.cloudapi.wayline;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskFile {

    /**
     * File URL
     */
    @NotNull
    private String url;

    /**
     * File signature
     */
    @NotNull
    private String fingerprint;

    public FlighttaskFile() {
    }

    @Override
    public String toString() {
        return "FlighttaskFile{" +
                "url='" + url + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public FlighttaskFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public FlighttaskFile setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }
}
