package com.dji.sample.media.service;

import com.dji.sdk.cloudapi.media.MediaUploadCallbackRequest;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
public interface IMediaService {

    /**
     * Check if the file has been uploaded by the fingerprint.
     * @param workspaceId
     * @param fingerprint
     * @return
     */
    Boolean fastUpload(String workspaceId, String fingerprint);

    /**
     * Save the basic information of the file to the database.
     * @param workspaceId
     * @param file
     * @return
     */
    Integer saveMediaFile(String workspaceId, MediaUploadCallbackRequest file);

    /**
     * Query tiny fingerprints about all files in this workspace based on the workspace id.
     * @param workspaceId
     * @return
     */
    List<String> getAllTinyFingerprintsByWorkspaceId(String workspaceId);

    /**
     * Query the fingerprints that already exist in it based on the incoming tiny fingerprints data.
     * @param workspaceId
     * @param tinyFingerprints
     * @return
     */
    List<String> getExistTinyFingerprints(String workspaceId, List<String> tinyFingerprints);

}
