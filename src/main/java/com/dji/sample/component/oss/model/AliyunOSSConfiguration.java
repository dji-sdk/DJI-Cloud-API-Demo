package com.dji.sample.component.oss.model;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Configuration
public class AliyunOSSConfiguration {

    /**
     * default
     */
    public static final String PROVIDER = "ali";

    /**
     * Whether to use the current storage service.
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

    @Value("${aliyun.oss.endpoint}")
    private void setEndpoint(String endpoint) {
        AliyunOSSConfiguration.endpoint = endpoint;
    }

    @Value("${aliyun.oss.access-key}")
    private void setAccessKey(String accessKey) {
        AliyunOSSConfiguration.accessKey = accessKey;
    }

    @Value("${aliyun.oss.secret-key}")
    private void setSecretKey(String secretKey) {
        AliyunOSSConfiguration.secretKey = secretKey;
    }

    @Value("${aliyun.oss.region}")
    private void setRegion(String region) {
        AliyunOSSConfiguration.region = region;
    }

    @Value("${aliyun.oss.expire: 3600}")
    private void setExpire(Long expire) {
        AliyunOSSConfiguration.expire = expire;
    }

    @Value("${aliyun.oss.enable: false}")
    private void setEnable(boolean enable) {
        AliyunOSSConfiguration.enable = enable;
    }

    @Value("${aliyun.oss.role-session-name}")
    private void setRoleSessionName(String roleSessionName) {
        AliyunOSSConfiguration.roleSessionName = roleSessionName;
    }

    @Value("${aliyun.oss.role-arn}")
    private void setRoleArn(String roleArn) {
        AliyunOSSConfiguration.roleArn = roleArn;
    }

    @Value("${aliyun.oss.bucket}")
    private void setBucket(String bucket) {
        AliyunOSSConfiguration.bucket = bucket;
    }

    @Value("${aliyun.oss.object-dir-prefix: wayline}")
    private void setObjectDir(String objectDirPrefix) {
        AliyunOSSConfiguration.objectDirPrefix = objectDirPrefix;
    }

    @Bean
    @Lazy
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKey, secretKey);
    }
}
