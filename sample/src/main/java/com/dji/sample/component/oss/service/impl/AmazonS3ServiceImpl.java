package com.dji.sample.component.oss.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.dji.sample.component.AuthInterceptor;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sdk.cloudapi.storage.CredentialsToken;
import com.dji.sdk.cloudapi.storage.OssTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/27
 */
@Slf4j
@Service
public class AmazonS3ServiceImpl implements IOssService {

    private AmazonS3 client;
    
    @Override
    public OssTypeEnum getOssType() {
        return OssTypeEnum.AWS;
    }

    @Override
    public CredentialsToken getCredentials() {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(OssConfiguration.accessKey, OssConfiguration.secretKey)))
                .withRegion(OssConfiguration.region).build();

        AssumeRoleRequest request = new AssumeRoleRequest()
                .withRoleArn(OssConfiguration.roleArn)
                .withRoleSessionName(OssConfiguration.roleSessionName)
                .withDurationSeconds(Math.toIntExact(OssConfiguration.expire));
        AssumeRoleResult result = stsClient.assumeRole(request);
        Credentials credentials = result.getCredentials();
        return new CredentialsToken(credentials.getAccessKeyId(), credentials.getSecretAccessKey(),
                credentials.getSessionToken(), (credentials.getExpiration().getTime() - System.currentTimeMillis()) / 1000);
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        return client.generatePresignedUrl(bucket, objectKey,
                new Date(System.currentTimeMillis() + OssConfiguration.expire * 1000), HttpMethod.GET);
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        if (!client.doesObjectExist(bucket, objectKey)) {
            return true;
        }
        client.deleteObject(bucket, objectKey);
        return true;
    }

    public InputStream getObject(String bucket, String objectKey) {
        return client.getObject(bucket, objectKey).getObjectContent().getDelegateStream();
    }

    @Override
    public void putObject(String bucket, String objectKey, InputStream input) {
        if (client.doesObjectExist(bucket, objectKey)) {
            throw new RuntimeException("The filename already exists.");
        }
        PutObjectResult objectResult = client.putObject(new PutObjectRequest(bucket, objectKey, input, new ObjectMetadata()));
        log.info("Upload FlighttaskCreateFile: {}", objectResult.toString());
    }

    public void createClient() {
        if (Objects.nonNull(this.client)) {
            return;
        }
        this.client = AmazonS3ClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(OssConfiguration.accessKey, OssConfiguration.secretKey)))
                .withRegion(OssConfiguration.region)
                .build();
    }

    /**
     * Configuring cross-origin resource sharing
     */
    @PostConstruct
    private void configCORS() {
        if (!OssConfiguration.enable || !OssTypeEnum.AWS.getType().equals(OssConfiguration.provider)) {
            return;
        }
        List<CORSRule.AllowedMethods> allowedMethods = new ArrayList<>();
        allowedMethods.add(CORSRule.AllowedMethods.GET);
        allowedMethods.add(CORSRule.AllowedMethods.POST);
        allowedMethods.add(CORSRule.AllowedMethods.DELETE);

        CORSRule rule = new CORSRule()
                .withId("CORSAccessRule")
                .withAllowedOrigins(List.of("*"))
                .withAllowedHeaders(List.of(AuthInterceptor.PARAM_TOKEN))
                .withAllowedMethods(allowedMethods);

        client.setBucketCrossOriginConfiguration(OssConfiguration.bucket,
                new BucketCrossOriginConfiguration().withRules(rule));
        
    }
}
