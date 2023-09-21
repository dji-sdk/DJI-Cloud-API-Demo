package com.dji.sample.storage.controller;

import com.dji.sample.storage.service.IStorageService;
import com.dji.sdk.cloudapi.storage.StsCredentialsResponse;
import com.dji.sdk.cloudapi.storage.api.IHttpStorageService;
import com.dji.sdk.common.HttpResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
@RestController
public class StorageController implements IHttpStorageService {

    @Autowired
    private IStorageService storageService;

    /**
     * Get temporary credentials for uploading the media and wayline in DJI Pilot.
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<StsCredentialsResponse> getTemporaryCredential(String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        StsCredentialsResponse stsCredentials = storageService.getSTSCredentials();
        return HttpResultResponse.success(stsCredentials);
    }
}
