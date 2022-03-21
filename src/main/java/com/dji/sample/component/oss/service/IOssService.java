package com.dji.sample.component.oss.service;

import com.dji.sample.media.model.CredentialsDTO;

import java.net.URL;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
public interface IOssService {

    /**
     * Get temporary credentials.
     * @return
     */
    CredentialsDTO getCredentials();

    /**
     * Get the address of the object based on the bucket name and the object name.
     * @param bucket    bucket name
     * @param objectKey object name
     * @return download link
     */
    URL getObjectUrl(String bucket, String objectKey);
}
