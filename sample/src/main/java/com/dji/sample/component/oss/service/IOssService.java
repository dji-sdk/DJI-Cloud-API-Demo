package com.dji.sample.component.oss.service;

import com.dji.sdk.cloudapi.storage.CredentialsToken;
import com.dji.sdk.cloudapi.storage.OssTypeEnum;

import java.io.InputStream;
import java.net.URL;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
public interface IOssService {

    OssTypeEnum getOssType();

    /**
     * Get temporary credentials.
     * @return
     */
    CredentialsToken getCredentials();

    /**
     * Get the address of the object based on the bucket name and the object name.
     * @param bucket    bucket name
     * @param objectKey object name
     * @return download link
     */
    URL getObjectUrl(String bucket, String objectKey);

    /**
     * Deletes the object in the storage bucket.
     * @param bucket
     * @param objectKey
     * @return
     */
    Boolean deleteObject(String bucket, String objectKey);

    /**
     * Get the contents of an object.
     * @param bucket
     * @param objectKey
     * @return
     */
    InputStream getObject(String bucket, String objectKey);

    void putObject(String bucket, String objectKey, InputStream input);

    void createClient();
}
