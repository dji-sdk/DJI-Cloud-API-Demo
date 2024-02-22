package com.dji.sample.manage.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.manage.model.dto.DeviceLogsDTO;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsGetParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sample.manage.service.IDeviceLogsService;
import com.dji.sdk.cloudapi.log.FileUploadUpdateRequest;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class DeviceLogsController {

    @Autowired
    private IDeviceLogsService deviceLogsService;

    /**
     * Obtain the device upload log list by paging according to the query parameters.
     * @param workspaceId
     * @param deviceSn
     * @param param
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}/logs-uploaded")
    public HttpResultResponse getUploadedLogs(DeviceLogsQueryParam param, @PathVariable("workspace_id") String workspaceId,
                                              @PathVariable("device_sn") String deviceSn) {
        PaginationData<DeviceLogsDTO> data = deviceLogsService.getUploadedLogs(deviceSn, param);
        return HttpResultResponse.success(data);
    }

    /**
     * Get a list of log files that can be uploaded in real time.
     * @param workspaceId
     * @param deviceSn
     * @param param
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse getLogsBySn(@PathVariable("workspace_id") String workspaceId,
                                          @PathVariable("device_sn") String deviceSn,
                                          DeviceLogsGetParam param) {
        return deviceLogsService.getRealTimeLogs(deviceSn, param.getDomainList());
    }

    /**
     * Initiate a log upload request to the gateway.
     * @return
     */
    @PostMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse uploadLogs(@PathVariable("workspace_id") String workspaceId,
                                         @PathVariable("device_sn") String deviceSn,
                                         HttpServletRequest request, @RequestBody DeviceLogsCreateParam param) {

        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);

        return deviceLogsService.pushFileUpload(customClaim.getUsername(), deviceSn, param);
    }

    /**
     * Cancel logs file upload.
     * @return
     */
    @DeleteMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse cancelUploadedLogs(@PathVariable("workspace_id") String workspaceId,
                                                 @PathVariable("device_sn") String deviceSn,
                                                 @RequestBody FileUploadUpdateRequest param) {

        return deviceLogsService.pushUpdateFile(deviceSn, param);
    }

    /**
     * Delete upload history.
     * @return
     */
    @DeleteMapping("/{workspace_id}/devices/{device_sn}/logs/{logs_id}")
    public HttpResultResponse deleteUploadedLogs(@PathVariable("workspace_id") String workspaceId,
                                                 @PathVariable("device_sn") String deviceSn,
                                                 @PathVariable("logs_id") String logsId) {
        deviceLogsService.deleteLogs(deviceSn, logsId);
        return HttpResultResponse.success();
    }
    /**
     * Query the download address of the file according to the wayline file id,
     * and redirect to this address directly for download.
     * @param workspaceId
     * @param fileId
     * @param logsId
     * @param response
     */
    @GetMapping("/{workspace_id}/logs/{logs_id}/url/{file_id}")
    public HttpResultResponse getFileUrl(@PathVariable(name = "workspace_id") String workspaceId,
                                         @PathVariable(name = "file_id") String fileId,
                                         @PathVariable(name = "logs_id") String logsId, HttpServletResponse response) {

        try {
            URL url = deviceLogsService.getLogsFileUrl(logsId, fileId);
            return HttpResultResponse.success(url.toString());
        } catch (Exception e) {
            log.error("Failed to get the logs file download address.");
            e.printStackTrace();
        }
        return HttpResultResponse.error("Failed to get the logs file download address.");
    }
}
