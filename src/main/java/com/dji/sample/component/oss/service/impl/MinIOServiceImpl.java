package com.dji.sample.component.oss.service.impl;

import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.model.enums.OssTypeEnum;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.credentials.AssumeRoleProvider;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
@Service
@Slf4j
public class MinIOServiceImpl implements IOssService {

    @Autowired
    private OssConfiguration configuration;

    @Override
    public String getOssType() {
        return OssTypeEnum.MINIO.getType();
    }

    @Override
    public CredentialsDTO getCredentials() {
        try {
            AssumeRoleProvider provider = new AssumeRoleProvider(configuration.getEndpoint(), configuration.getAccessKey(),
                    configuration.getSecretKey(), Math.toIntExact(configuration.getExpire()),
                    null, configuration.getRegion(), null, null, null, null);
            return new CredentialsDTO(provider.fetch(), configuration.getExpire());
        } catch (NoSuchAlgorithmException e) {
            log.debug("Failed to obtain sts.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        try {
            return new URL(
                    this.createClient()
                            .getPresignedObjectUrl(
                                    GetPresignedObjectUrlArgs.builder()
                                            .method(Method.GET)
                                            .bucket(bucket)
                                            .object(objectKey)
                                            .expiry(Math.toIntExact(configuration.getExpire()))
                                            .build()));
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                InvalidKeyException | InvalidResponseException | IOException |
                NoSuchAlgorithmException | XmlParserException | ServerException e) {
            log.error("The file does not exist on the OssConfiguration.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        MinioClient client = this.createClient();
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error("Failed to delete file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public byte[] getObject(String bucket, String objectKey) {
        MinioClient client = this.createClient();
        try (InputStream objectResponse = client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectKey).build())) {
            return objectResponse.readAllBytes();
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private MinioClient createClient() {
        return MinioClient.builder()
                .endpoint(configuration.getEndpoint())
                .credentials(configuration.getAccessKey(), configuration.getSecretKey())
                .region(configuration.getRegion())
                .build();
    }
}
