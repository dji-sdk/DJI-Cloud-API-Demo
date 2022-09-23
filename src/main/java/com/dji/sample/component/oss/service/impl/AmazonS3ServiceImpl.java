package com.dji.sample.component.oss.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.dji.sample.component.AuthInterceptor;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.model.enums.OssTypeEnum;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/27
 */
@Service
public class AmazonS3ServiceImpl implements IOssService {

    @Autowired
    private OssConfiguration configuration;

    @Override
    public String getOssType() {
        return OssTypeEnum.AWS.getType();
    }

    @Override
    public CredentialsDTO getCredentials() {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey())))
                .withRegion(configuration.getRegion()).build();

        AssumeRoleRequest request = new AssumeRoleRequest()
                .withRoleArn(configuration.getRoleArn())
                .withRoleSessionName(configuration.getRoleSessionName())
                .withDurationSeconds(Math.toIntExact(configuration.getExpire()));
        AssumeRoleResult result = stsClient.assumeRole(request);
        Credentials credentials = result.getCredentials();
        stsClient.shutdown();
        return new CredentialsDTO(credentials);
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        AmazonS3 client = this.createClient();
        URL url = client.generatePresignedUrl(bucket, objectKey,
                new Date(System.currentTimeMillis() + configuration.getExpire() * 1000), HttpMethod.GET);
        client.shutdown();
        return url;
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        AmazonS3 client = this.createClient();
        if (!client.doesObjectExist(bucket, objectKey)) {
            client.shutdown();
            return true;
        }
        client.deleteObject(bucket, objectKey);
        client.shutdown();
        return true;
    }

    public byte[] getObject(String bucket, String objectKey) {
        AmazonS3 client = this.createClient();
        S3Object object = client.getObject(bucket, objectKey);

        try (InputStream stream = object.getObjectContent().getDelegateStream()) {
            return stream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
        return new byte[0];
    }

    private AmazonS3 createClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey())))
                .withRegion(configuration.getRegion())
                .build();
    }

    /**
     * Configuring cross-origin resource sharing
     */
    @PostConstruct
    private void configCORS() {
        if (!configuration.isEnable() || !OssTypeEnum.AWS.getType().equals(configuration.getProvider())) {
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

        AmazonS3 client = this.createClient();

        client.setBucketCrossOriginConfiguration(this.configuration.getBucket(),
                new BucketCrossOriginConfiguration().withRules(rule));
        client.shutdown();
    }
}
