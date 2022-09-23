package com.dji.sample.component.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.model.enums.OssTypeEnum;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
@Service
@Slf4j
public class AliyunOssServiceImpl implements IOssService {

    @Autowired
    public OssConfiguration configuration;

    @Override
    public String getOssType() {
        return OssTypeEnum.ALIYUN.getType();
    }

    @Override
    public CredentialsDTO getCredentials() {

        try {
            DefaultProfile profile = DefaultProfile.getProfile(
                    configuration.getRegion(), configuration.getAccessKey(), configuration.getSecretKey());
            IAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setDurationSeconds(configuration.getExpire());
            request.setRoleArn(configuration.getRoleArn());
            request.setRoleSessionName(configuration.getRoleSessionName());

            AssumeRoleResponse response = client.getAcsResponse(request);
            return new CredentialsDTO(response.getCredentials(), configuration.getExpire());

        } catch (ClientException e) {
            log.debug("Failed to obtain sts.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            return null;
        }
        OSS ossClient = this.createClient();
        // First check if the object can be fetched.
        boolean isExist = ossClient.doesObjectExist(bucket, objectKey);
        if (!isExist) {
            throw new OSSException("The object does not exist.");
        }

        return ossClient.generatePresignedUrl(bucket, objectKey,
                new Date(System.currentTimeMillis() + configuration.getExpire() * 1000));
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        OSS ossClient = this.createClient();
        if (!ossClient.doesObjectExist(bucket, objectKey)) {
            ossClient.shutdown();
            return true;
        }
        ossClient.deleteObject(bucket, objectKey);
        ossClient.shutdown();
        return true;
    }

    @Override
    public byte[] getObject(String bucket, String objectKey) {
        OSS ossClient = this.createClient();
        OSSObject object = ossClient.getObject(bucket, objectKey);

        try (InputStream stream = object.getObjectContent()) {
            return stream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return new byte[0];
    }

    private OSS createClient() {
        return new OSSClientBuilder()
                .build(configuration.getEndpoint(), configuration.getAccessKey(), configuration.getSecretKey());
    }
}
