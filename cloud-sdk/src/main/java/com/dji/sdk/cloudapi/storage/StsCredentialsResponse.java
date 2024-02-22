package com.dji.sdk.cloudapi.storage;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "Temporary credential data")
public class StsCredentialsResponse extends BaseModel {

    @Schema(description = "bucket name", example = "bucket-api")
    @NotNull
    private String bucket;

    @NotNull
    @Valid
    @Schema(description = "The token data of the temporary credential")
    private CredentialsToken credentials;

    @NotNull
    @Schema(description = "access domain name for external services", example = "https://oss-cn-hangzhou.aliyuncs.com")
    @Pattern(regexp = "^http[s]?://.*$")
    private String endpoint;

    @NotNull
    @JsonProperty("object_key_prefix")
    @Schema(description = "The folder path where the object needs to be stored.", example = "files/wayline")
    private String objectKeyPrefix;

    @NotNull
    private OssTypeEnum provider;

    @NotNull
    @Schema(description = "The region where the bucket is located.", example = "us-east-1")
    private String region;

    public StsCredentialsResponse() {
    }

    @Override
    public String toString() {
        return "StsCredentialsResponse{" +
                "bucket='" + bucket + '\'' +
                ", credentials=" + credentials +
                ", endpoint='" + endpoint + '\'' +
                ", objectKeyPrefix='" + objectKeyPrefix + '\'' +
                ", provider='" + provider + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    public String getBucket() {
        return bucket;
    }

    public StsCredentialsResponse setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CredentialsToken getCredentials() {
        return credentials;
    }

    public StsCredentialsResponse setCredentials(CredentialsToken credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public StsCredentialsResponse setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getObjectKeyPrefix() {
        return objectKeyPrefix;
    }

    public StsCredentialsResponse setObjectKeyPrefix(String objectKeyPrefix) {
        this.objectKeyPrefix = objectKeyPrefix;
        return this;
    }

    public OssTypeEnum getProvider() {
        return provider;
    }

    public StsCredentialsResponse setProvider(OssTypeEnum provider) {
        this.provider = provider;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public StsCredentialsResponse setRegion(String region) {
        this.region = region;
        return this;
    }
}
