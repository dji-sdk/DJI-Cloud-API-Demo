package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.LogsFileDTO;
import com.dji.sample.manage.model.dto.LogsFileUploadDTO;
import com.dji.sdk.cloudapi.log.LogFileIndex;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
public interface ILogsFileIndexService {

    /**
     * Insert the index of the device logs.
     * @param file
     * @param deviceSn
     * @param domain
     * @param fileId
     * @return
     */
    Boolean insertFileIndex(LogFileIndex file, String deviceSn, Integer domain, String fileId);

    /**
     * Query logs file upload information based on the file id.
     * @param fileId
     * @return
     */
    Optional<LogsFileUploadDTO> getFileIndexByFileId(String fileId);

    /**
     * Batch query logs file upload information.
     * @param fileIds
     * @return
     */
    List<LogsFileUploadDTO> getFileIndexByFileIds(List<LogsFileDTO> fileIds);

    /**
     * Delete log index data based on file id.
     * @param fileIds
     */
    void deleteFileIndexByFileIds(List<String> fileIds);
}
