package com.dji.sample.storage.service.impl;

import com.dji.sample.component.oss.model.MinIOConfiguration;
import com.dji.sample.component.oss.service.impl.MinIOServiceImpl;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.dji.sample.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/31
 */
@Service
public class MinIOStorageServiceImpl implements IStorageService {

    @Autowired
    private MinIOServiceImpl ossService;

    @Override
    public StsCredentialsDTO getSTSCredentials() {
        return StsCredentialsDTO.builder()
                .endpoint(MinIOConfiguration.endpoint)
                .bucket(MinIOConfiguration.bucket)
                .credentials(ossService.getCredentials())
                .provider(MinIOConfiguration.PROVIDER)
                .objectKeyPrefix(MinIOConfiguration.objectDirPrefix)
                .region(MinIOConfiguration.region)
                .build();
    }


}
