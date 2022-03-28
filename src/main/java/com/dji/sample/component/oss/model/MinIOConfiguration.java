package com.dji.sample.component.oss.model;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Configuration
public class MinIOConfiguration {

    /**
     * default
     */
    public static final String PROVIDER = "aws";

    /**
     * Whether to use the current storage service.
     */
    public static boolean enable;

    public static String endpoint;

    public static String accessKey;

    public static String secretKey;

    public static String region;

    public static String bucket;

    public static Integer expire;

    public static String objectDirPrefix;

    @Value("${minio.endpoint: http://localhost:9000/}")
    private void setEndpoint(String endpoint) {
        MinIOConfiguration.endpoint = endpoint;
    }

    @Value("${minio.access-key: minioadmin}")
    private void setAccessKey(String accessKey) {
        MinIOConfiguration.accessKey = accessKey;
    }

    @Value("${minio.secret-key: minioadmin}")
    private void setSecretKey(String secretKey) {
        MinIOConfiguration.secretKey = secretKey;
    }

    @Value("${minio.region: }")
    private void setRegion(String region) {
        MinIOConfiguration.region = region;
    }

    @Value("${minio.bucket: test}")
    private void setBucket(String bucket) {
        MinIOConfiguration.bucket = bucket;
    }

    @Value("${minio.expire: 3600}")
    private void setExpire(Integer expire) {
        MinIOConfiguration.expire = expire;
    }

    @Value("${minio.enable: false}")
    private void setEnable(boolean enable) {
        MinIOConfiguration.enable = enable;
    }

    @Value("${minio.object-dir-prefix: wayline}")
    private void setObjectDir(String objectDirPrefix) {
        MinIOConfiguration.objectDirPrefix = objectDirPrefix;
    }

    @Bean
    @Lazy
    public MinioClient minioClient() {
        if (!enable) {
            return null;
        }
        MinioClient.Builder builder = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey);
        if (StringUtils.hasText(region)) {
            builder.region(MinIOConfiguration.region);
        }
        return builder.build();
    }
}
