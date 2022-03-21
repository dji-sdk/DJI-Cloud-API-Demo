package com.dji.sample.media.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.media.model.FileUploadDTO;
import com.dji.sample.media.service.IMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Slf4j
@RestController
@RequestMapping("${url.media.prefix}${url.media.version}/workspaces")
public class MediaController {

    @Autowired
    private IMediaService mediaService;

    /**
     * Check if the file has been uploaded by the fingerprint.
     * @param workspaceId
     * @param file
     * @return
     */
    @PostMapping("/{workspace_id}/fast-upload")
    public ResponseResult fastUpload(@PathVariable(name = "workspace_id") String workspaceId, @RequestBody FileUploadDTO file) {

        boolean isExist = mediaService.fastUpload(workspaceId, file.getFingerprint());

        return isExist ? ResponseResult.success() : ResponseResult.error(file.getFingerprint() + "already exists.");
    }

    /**
     * When the file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param workspaceId
     * @param file
     * @return
     */
    @PostMapping("/{workspace_id}/upload-callback")
    public ResponseResult<String> uploadCallback(@PathVariable(name = "workspace_id") String workspaceId, @RequestBody FileUploadDTO file) {
        mediaService.saveMediaFile(workspaceId, file);
        return ResponseResult.success(file.getObjectKey());

    }

    /**
     * Query the files that already exist in this workspace based on the workspace id and the collection of tiny fingerprints.
     * @param workspaceId
     * @param tinyFingerprints
     * @return
     */
    @GetMapping("/{workspace_id}/files/tiny-fingerprints")
    public ResponseResult<Map<String, List<String>>> uploadCallback(@PathVariable(name = "workspace_id") String workspaceId,
                               @RequestParam(value = "tiny_fingerprint") List<String> tinyFingerprints) {
        List<String> tinyFingerprintList = mediaService.getAllTinyFingerprintsByWorkspaceId(workspaceId);
        List<String> existingList = tinyFingerprints
                .stream()
                .filter(tinyFingerprintList::contains)
                .collect(Collectors.toList());
        return ResponseResult.success(new ConcurrentHashMap<>(Map.of("tiny_fingerprints", existingList)));
    }

}