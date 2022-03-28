package com.dji.sample.component.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.dji.sample.component.oss.model.AliyunOSSConfiguration;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired(required = false)
    private OSS ossClient;

    @Override
    public CredentialsDTO getCredentials() {

        try {
            DefaultProfile profile = DefaultProfile.getProfile(
                    AliyunOSSConfiguration.region, AliyunOSSConfiguration.accessKey, AliyunOSSConfiguration.secretKey);
            IAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setDurationSeconds(AliyunOSSConfiguration.expire);
            request.setRoleArn(AliyunOSSConfiguration.roleArn);
            request.setRoleSessionName(AliyunOSSConfiguration.roleSessionName);

            AssumeRoleResponse response = client.getAcsResponse(request);
            return new CredentialsDTO(response.getCredentials(), AliyunOSSConfiguration.expire);

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
        try {
            // First check if the object can be fetched.
            ossClient.getObject(bucket, objectKey);

            return ossClient.generatePresignedUrl(bucket, objectKey,
                    new Date(System.currentTimeMillis() + AliyunOSSConfiguration.expire * 1000));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
