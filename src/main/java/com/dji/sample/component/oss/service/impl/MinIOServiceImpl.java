package com.dji.sample.component.oss.service.impl;

import com.dji.sample.component.oss.model.MinIOConfiguration;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.credentials.AssumeRoleProvider;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private MinioClient client;

    @Override
    public CredentialsDTO getCredentials() {
        try {
            AssumeRoleProvider provider = new AssumeRoleProvider(MinIOConfiguration.endpoint, MinIOConfiguration.accessKey,
                    MinIOConfiguration.secretKey, MinIOConfiguration.expire,
                    null, null, null, null, null, null);
            return new CredentialsDTO(provider.fetch(), MinIOConfiguration.expire);
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
                    client.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucket)
                                    .object(objectKey)
                                    .expiry(MinIOConfiguration.expire)
                                    .build()));
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                InvalidKeyException | InvalidResponseException | IOException |
                NoSuchAlgorithmException | XmlParserException | ServerException e) {
            log.error("The file does not exist on the oss.");
            e.printStackTrace();
        }
        return null;
    }

}
