package com.dji.sample.component.oss.service.impl;

import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.model.enums.OssTypeEnum;
import com.dji.sample.component.oss.service.IOssService;
import com.dji.sample.media.model.CredentialsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/30
 */
@Service
public class OssServiceContext {

    private IOssService ossService;

    private OssConfiguration configuration;

    @Autowired
    public OssServiceContext(List<IOssService> ossServices, OssConfiguration configuration) {
        this.configuration = configuration;
        if (!configuration.isEnable()) {
            return;
        }
        this.ossService = ossServices.stream()
                .filter(ossService -> ossService.getOssType().equals(configuration.getProvider()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Oss provider is illegal. Optional: " +
                        Arrays.toString(Arrays.stream(OssTypeEnum.values()).map(OssTypeEnum::getType).toArray())));
    }

    IOssService getOssService() {
        return this.ossService;
    }

    public CredentialsDTO getCredentials() {
        return this.ossService.getCredentials();
    }

    public URL getObjectUrl(String bucket, String objectKey) {
        return this.ossService.getObjectUrl(bucket, objectKey);
    }

    public Boolean deleteObject(String bucket, String objectKey) {
        return this.ossService.deleteObject(bucket, objectKey);
    }

    public byte[] getObject(String bucket, String objectKey) {
        return this.ossService.getObject(bucket, objectKey);
    }
}
