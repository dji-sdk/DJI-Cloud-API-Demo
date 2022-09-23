package com.dji.sample.manage.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.dto.DeviceLogsDTO;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sample.manage.model.param.LogsFileUpdateParam;
import org.springframework.messaging.MessageHeaders;

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
    ResponseResult getRealTimeLogs(String deviceSn, List<String> domainList);

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
    ResponseResult pushFileUpload(String username, String deviceSn, DeviceLogsCreateParam param);

    /**
     * Push a request to modify the  status of the logs file.
     * @param deviceSn
     * @param param
     * @return
     */
    ResponseResult pushUpdateFile(String deviceSn, LogsFileUpdateParam param);

    /**
     * Delete log records.
     * @param deviceSn
     * @param logsId
     */
    void deleteLogs(String deviceSn, String logsId);

    /**
     * Handle logs file upload progress.
     * @param receiver
     * @param headers
     */
    void handleFileUploadProgress(CommonTopicReceiver receiver, MessageHeaders headers);

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
