package com.dji.sample.storage.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.oss.model.AliyunOSSConfiguration;
import com.dji.sample.component.oss.model.MinIOConfiguration;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.dji.sample.storage.service.IStorageService;
import com.dji.sample.storage.service.impl.AliyunStorageServiceImpl;
import com.dji.sample.storage.service.impl.MinIOStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
@RestController
@RequestMapping("${url.storage.prefix}${url.storage.version}/workspaces/")
@Slf4j
public class StorageController {

    private IStorageService storageService;

    @Autowired
    private void setOssService(@Autowired(required = false) AliyunStorageServiceImpl aliyunStorageService,
                              @Autowired(required = false) MinIOStorageServiceImpl minIOStorageService) {
        if (AliyunOSSConfiguration.enable) {
            this.storageService = aliyunStorageService;
            return;
        }
        if (MinIOConfiguration.enable) {
            this.storageService = minIOStorageService;
            return;
        }
        log.error("storageService is null.");
    }
    /**
     * Get temporary credentials for uploading the media and wayline in DJI Pilot.
     * @param workspaceId
     * @return
     */
    @PostMapping("/{workspace_id}/sts")
    public ResponseResult<StsCredentialsDTO> getSTSCredentials(@PathVariable(name = "workspace_id") String workspaceId) {

        StsCredentialsDTO stsCredentials = storageService.getSTSCredentials();
        return ResponseResult.success(stsCredentials);
    }
}
