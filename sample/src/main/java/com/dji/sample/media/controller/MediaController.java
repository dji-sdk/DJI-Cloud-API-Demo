package com.dji.sample.media.controller;

import com.dji.sample.media.service.IMediaService;
import com.dji.sdk.cloudapi.media.*;
import com.dji.sdk.cloudapi.media.api.IHttpMediaService;
import com.dji.sdk.common.HttpResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Slf4j
@RestController
public class MediaController implements IHttpMediaService {

    @Autowired
    private IMediaService mediaService;

    /**
     * Check if the file has been uploaded by the fingerprint.
     * @param workspaceId
     * @param request
     * @return
     */
    @Override
    public HttpResultResponse mediaFastUpload(String workspaceId, @Valid MediaFastUploadRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isExist = mediaService.fastUpload(workspaceId, request.getFingerprint());

        return isExist ? HttpResultResponse.success() : HttpResultResponse.error(request.getFingerprint() + "don't exist.");
    }

    /**
     * When the file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param workspaceId
     * @param request
     * @return
     */
    @Override
    public HttpResultResponse<String> mediaUploadCallback(String workspaceId, @Valid MediaUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        mediaService.saveMediaFile(workspaceId, request);
        return HttpResultResponse.success(request.getObjectKey());
    }

    /**
     * Query the files that already exist in this workspace based on the workspace id and the collection of tiny fingerprints.
     * @param workspaceId
     * @param request  There is only one tiny_fingerprint parameter in the body.
     *                          But it is not recommended to use Map to receive the parameter.
     * @return
     */
    @Override
    public HttpResultResponse<GetFileFingerprintResponse> getExistFileTinyFingerprint(String workspaceId, @Valid GetFileFingerprintRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        List<String> existingList = mediaService.getExistTinyFingerprints(workspaceId, request.getTinyFingerprints());
        return HttpResultResponse.success(new GetFileFingerprintResponse().setTinyFingerprints(existingList));
    }

    @Override
    public HttpResultResponse folderUploadCallback(String workspaceId, @Valid FolderUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        return null;
    }
}