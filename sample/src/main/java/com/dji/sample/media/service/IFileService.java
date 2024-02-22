package com.dji.sample.media.service;

import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sdk.cloudapi.media.MediaUploadCallbackRequest;
import com.dji.sdk.common.PaginationData;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
public interface IFileService {

    /**
     * Query if the file already exists based on the workspace id and the fingerprint of the file.
     * @param workspaceId
     * @param fingerprint
     * @return
     */
    Boolean checkExist(String workspaceId, String fingerprint);

    /**
     * Save the basic information of the file to the database.
     * @param workspaceId
     * @param file
     * @return
     */
    Integer saveFile(String workspaceId, MediaUploadCallbackRequest file);

    /**
     * Query information about all files in this workspace based on the workspace id.
     * @param workspaceId
     * @return
     */
    List<MediaFileDTO> getAllFilesByWorkspaceId(String workspaceId);

    /**
     * Paginate through all media files in this workspace.
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    PaginationData<MediaFileDTO> getMediaFilesPaginationByWorkspaceId(String workspaceId, long page, long pageSize);

    /**
     * Get the download address of the file.
     * @param workspaceId
     * @param fileId
     * @return
     */
    URL getObjectUrl(String workspaceId, String fileId);

    /**
     * Query all media files of a job.
     * @param workspaceId
     * @param jobId
     * @return
     */
    List<MediaFileDTO> getFilesByWorkspaceAndJobId(String workspaceId, String jobId);
}
