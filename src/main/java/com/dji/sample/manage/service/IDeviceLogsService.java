package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.DeviceLogsDTO;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sdk.cloudapi.log.FileUploadUpdateRequest;
import com.dji.sdk.cloudapi.log.LogModuleEnum;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public interface IDeviceLogsService {

    /**
     * Obtain the device upload log list by paging according to the query parameters.
     * @param deviceSn
     * @param param
     * @return
     */
    PaginationData<DeviceLogsDTO> getUploadedLogs(String deviceSn, DeviceLogsQueryParam param);

    /**
     * Get a list of log files that can be uploaded in real time.
     * @param deviceSn
     * @param domainList
     * @return
     */
    HttpResultResponse getRealTimeLogs(String deviceSn, List<LogModuleEnum> domainList);

    /**
     * Add device logs.
     *
     * @param bid
     * @param username
     * @param deviceSn
     * @param param
     * @return logs id
     */
    String insertDeviceLogs(String bid, String username, String deviceSn, DeviceLogsCreateParam param);

    /**
     * Initiate a log upload request to the gateway.
     * @param username
     * @param deviceSn
     * @param param
     * @return
     */
    HttpResultResponse pushFileUpload(String username, String deviceSn, DeviceLogsCreateParam param);

    /**
     * Push a request to modify the  status of the logs file.
     * @param deviceSn
     * @param param
     * @return
     */
    HttpResultResponse pushUpdateFile(String deviceSn, FileUploadUpdateRequest param);

    /**
     * Delete log records.
     * @param deviceSn
     * @param logsId
     */
    void deleteLogs(String deviceSn, String logsId);

    /**
     * Update status, which is updated when the logs upload succeeds or fails.
     * @param logsId
     * @param value
     */
    void updateLogsStatus(String logsId, Integer value);

    /**
     * Get the file address.
     * @param logsId
     * @param fileId
     * @return
     */
    URL getLogsFileUrl(String logsId, String fileId);
}
