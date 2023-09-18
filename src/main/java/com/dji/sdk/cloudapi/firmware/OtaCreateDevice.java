package com.dji.sdk.cloudapi.firmware;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
public class OtaCreateDevice {

    @NotNull
    private String sn;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String productVersion;

    @NotNull
    private String fileUrl;

    @NotNull
    private String md5;

    @NotNull
    private Long fileSize;

    @NotNull
    private FirmwareUpgradeTypeEnum firmwareUpgradeType;

    @NotNull
    private String fileName;

    public OtaCreateDevice() {
    }

    @Override
    public String toString() {
        return "OtaCreateDevice{" +
                "sn='" + sn + '\'' +
                ", productVersion='" + productVersion + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", md5='" + md5 + '\'' +
                ", fileSize=" + fileSize +
                ", firmwareUpgradeType=" + firmwareUpgradeType +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public OtaCreateDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public OtaCreateDevice setProductVersion(String productVersion) {
        this.productVersion = productVersion;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public OtaCreateDevice setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public OtaCreateDevice setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public OtaCreateDevice setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public FirmwareUpgradeTypeEnum getFirmwareUpgradeType() {
        return firmwareUpgradeType;
    }

    public OtaCreateDevice setFirmwareUpgradeType(FirmwareUpgradeTypeEnum firmwareUpgradeType) {
        this.firmwareUpgradeType = firmwareUpgradeType;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public OtaCreateDevice setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
