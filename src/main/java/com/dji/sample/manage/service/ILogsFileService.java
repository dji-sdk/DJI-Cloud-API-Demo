package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.LogsFileDTO;
import com.dji.sample.manage.model.dto.LogsFileUploadDTO;
import com.dji.sdk.cloudapi.log.FileUploadProgressFile;
import com.dji.sdk.cloudapi.log.FileUploadStartFile;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public interface ILogsFileService {

    /**
     * Query the uploaded log file information based on the log id.
     * @param logsId
     * @return
     */
    List<LogsFileDTO> getLogsFileInfoByLogsId(String logsId);

    /**
     * Query the uploaded log file structure information based on the log id.
     * @param logsId
     * @return
     */
    List<LogsFileUploadDTO> getLogsFileByLogsId(String logsId);

    /**
     * Added logs file.
     * @param file
     * @param logsId
     * @return
     */
    Boolean insertFile(FileUploadStartFile file, String logsId);

    /**
     * Delete logs files.
     * @param logsId
     */
    void deleteFileByLogsId(String logsId);

    /**
     * Update file information.
     * @param logsId
     * @param fileReceiver
     */
    void updateFile(String logsId, FileUploadProgressFile fileReceiver);

    /**
     * Update file upload status.
     * @param logsId
     * @param isUploaded
     */
    void updateFileUploadStatus(String logsId, Boolean isUploaded);

    /**
     * Get the file address.
     * @param logsId
     * @param fileId
     * @return
     */
    URL getLogsFileUrl(String logsId, String fileId);
}
