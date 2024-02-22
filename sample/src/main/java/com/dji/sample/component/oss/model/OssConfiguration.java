package com.dji.sample.component.oss.model;

import com.dji.sdk.cloudapi.storage.OssTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@ConfigurationProperties(prefix = "oss")
@Component
public class OssConfiguration {

    /**
     * @see OssTypeEnum
     */
    public static OssTypeEnum provider;

    /**
     * Whether to use the object storage service.
     */
    public static boolean enable;

    /**
     * The protocol needs to be included at the beginning of the address.
     */
    public static String endpoint;

    public static String accessKey;

    public static String secretKey;

    public static String region;

    public static Long expire;

    public static String roleSessionName;

    public static String roleArn;

    public static String bucket;

    public static String objectDirPrefix;

    public void setProvider(OssTypeEnum provider) {
        OssConfiguration.provider = provider;
    }

    public void setEnable(boolean enable) {
        OssConfiguration.enable = enable;
    }

    public void setEndpoint(String endpoint) {
        OssConfiguration.endpoint = endpoint;
    }

    public void setAccessKey(String accessKey) {
        OssConfiguration.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        OssConfiguration.secretKey = secretKey;
    }

    public void setRegion(String region) {
        OssConfiguration.region = region;
    }

    public void setExpire(Long expire) {
        OssConfiguration.expire = expire;
    }

    public void setRoleSessionName(String roleSessionName) {
        OssConfiguration.roleSessionName = roleSessionName;
    }

    public void setRoleArn(String roleArn) {
        OssConfiguration.roleArn = roleArn;
    }

    public void setBucket(String bucket) {
        OssConfiguration.bucket = bucket;
    }

    public void setObjectDirPrefix(String objectDirPrefix) {
        OssConfiguration.objectDirPrefix = objectDirPrefix;
    }
}



