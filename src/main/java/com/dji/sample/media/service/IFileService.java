package com.dji.sample.media.service;

import com.dji.sample.media.model.FileUploadDTO;
import com.dji.sample.media.model.MediaFileDTO;

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
    Integer saveFile(String workspaceId, FileUploadDTO file);

    /**
     * Query information about all files in this workspace based on the workspace id.
     * @param workspaceId
     * @return
     */
    List<MediaFileDTO> getAllFilesByWorkspaceId(String workspaceId);
}
