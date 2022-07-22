package com.dji.sample.component.oss.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@ConfigurationProperties(prefix = "oss")
@Component
@Data
public class OssConfiguration {

    /**
     * @see com.dji.sample.component.oss.model.enums.OssTypeEnum
     */
    private String provider;

    /**
     * Whether to use the object storage service.
     */
    private boolean enable;

    /**
     * The protocol needs to be included at the beginning of the address.
     */
    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String region;

    private Long expire;

    private String roleSessionName;

    private String roleArn;

    private String bucket;

    private String objectDirPrefix;

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public void setRoleSessionName(String roleSessionName) {
        this.roleSessionName = roleSessionName;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setObjectDirPrefix(String objectDirPrefix) {
        this.objectDirPrefix = objectDirPrefix;
    }
}



