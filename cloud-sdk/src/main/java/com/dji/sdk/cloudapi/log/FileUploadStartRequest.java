package com.dji.sdk.cloudapi.log;

import com.dji.sdk.cloudapi.storage.CredentialsToken;
import com.dji.sdk.cloudapi.storage.OssTypeEnum;
import com.dji.sdk.cloudapi.storage.StsCredentialsResponse;
import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
public class FileUploadStartRequest extends BaseModel {

    @NotNull
    private String bucket;

    @NotNull
    @Valid
    private CredentialsToken credentials;

    @NotNull
    private String endpoint;

    @NotNull
    private String fileStoreDir;

    @NotNull
    private OssTypeEnum provider;

    @NotNull
    private String fileType = "text_log";

    @NotNull
    @Valid
    private FileUploadStartParam params;

    @NotNull
    private String region;

    public FileUploadStartRequest(StsCredentialsResponse sts) {
        this.bucket = sts.getBucket();
        long expire = sts.getCredentials().getExpire();
        sts.getCredentials().setExpire(System.currentTimeMillis() + (expire - 60) * 1000);
        this.credentials = sts.getCredentials();
        this.endpoint = sts.getEndpoint();
        this.fileStoreDir = sts.getObjectKeyPrefix();
        this.provider = sts.getProvider();
        this.region = sts.getRegion();
    }

    public FileUploadStartRequest() {
    }

    @Override
    public String toString() {
        return "FileUploadStartRequest{" +
                "bucket='" + bucket + '\'' +
                ", credentials=" + credentials +
                ", endpoint='" + endpoint + '\'' +
                ", fileStoreDir='" + fileStoreDir + '\'' +
                ", provider=" + provider +
                ", fileType='" + fileType + '\'' +
                ", params=" + params +
                ", region='" + region + '\'' +
                '}';
    }

    public String getBucket() {
        return bucket;
    }

    public FileUploadStartRequest setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CredentialsToken getCredentials() {
        return credentials;
    }

    public FileUploadStartRequest setCredentials(CredentialsToken credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public FileUploadStartRequest setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getFileStoreDir() {
        return fileStoreDir;
    }

    public FileUploadStartRequest setFileStoreDir(String fileStoreDir) {
        this.fileStoreDir = fileStoreDir;
        return this;
    }

    public OssTypeEnum getProvider() {
        return provider;
    }

    public FileUploadStartRequest setProvider(OssTypeEnum provider) {
        this.provider = provider;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public FileUploadStartParam getParams() {
        return params;
    }

    public FileUploadStartRequest setParams(FileUploadStartParam params) {
        this.params = params;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public FileUploadStartRequest setRegion(String region) {
        this.region = region;
        return this;
    }
}
