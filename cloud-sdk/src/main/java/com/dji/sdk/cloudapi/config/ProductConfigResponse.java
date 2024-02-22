package com.dji.sdk.cloudapi.config;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
public class ProductConfigResponse extends BaseModel {

    private String ntpServerHost;

    @NotNull
    private String appId;

    @NotNull
    private String appKey;

    @NotNull
    private String appLicense;

    public ProductConfigResponse() {
    }

    @Override
    public String toString() {
        return "ProductConfigResponse{" +
                "ntpServerHost='" + ntpServerHost + '\'' +
                ", appId='" + appId + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appLicense='" + appLicense + '\'' +
                '}';
    }

    public String getNtpServerHost() {
        return ntpServerHost;
    }

    public ProductConfigResponse setNtpServerHost(String ntpServerHost) {
        this.ntpServerHost = ntpServerHost;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public ProductConfigResponse setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public ProductConfigResponse setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getAppLicense() {
        return appLicense;
    }

    public ProductConfigResponse setAppLicense(String appLicense) {
        this.appLicense = appLicense;
        return this;
    }
}
