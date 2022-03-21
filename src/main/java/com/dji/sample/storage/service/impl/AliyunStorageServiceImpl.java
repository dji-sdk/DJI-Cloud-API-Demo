package com.dji.sample.storage.service.impl;

import com.dji.sample.component.oss.model.AliyunOSSConfiguration;
import com.dji.sample.component.oss.service.impl.AliyunOssServiceImpl;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.dji.sample.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/3/9
 */
@Service
public class AliyunStorageServiceImpl implements IStorageService {

    @Autowired
    private AliyunOssServiceImpl ossService;

    @Override
    public StsCredentialsDTO getSTSCredentials() {
        return StsCredentialsDTO.builder()
                .endpoint(AliyunOSSConfiguration.endpoint)
                .bucket(AliyunOSSConfiguration.bucket)
                .credentials(ossService.getCredentials())
                .provider(AliyunOSSConfiguration.PROVIDER)
                .objectKeyPrefix(AliyunOSSConfiguration.objectDirPrefix)
                .region(AliyunOSSConfiguration.region)
                .build();
    }
}
