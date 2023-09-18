package com.dji.sample.manage.controller;

import com.dji.sample.manage.model.dto.DeviceHmsDTO;
import com.dji.sample.manage.model.param.DeviceHmsQueryParam;
import com.dji.sample.manage.service.IDeviceHmsService;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */

@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/devices")
public class DeviceHmsController {

    @Autowired
    private IDeviceHmsService deviceHmsService;

    /**
     * Page to query the hms information of the device.
     * @param param
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/devices/hms")
    public HttpResultResponse<PaginationData<DeviceHmsDTO>> getHmsInformation(DeviceHmsQueryParam param,
                                                                              @PathVariable("workspace_id") String workspaceId) {
        PaginationData<DeviceHmsDTO> devices = deviceHmsService.getDeviceHmsByParam(param);

        return HttpResultResponse.success(devices);
    }

    /**
     * Update unread hms messages to read status.
     * @param deviceSn
     * @return
     */
    @PutMapping("/{workspace_id}/devices/hms/{device_sn}")
    public HttpResultResponse updateReadHmsByDeviceSn(@PathVariable("device_sn") String deviceSn) {
        deviceHmsService.updateUnreadHms(deviceSn);
        return HttpResultResponse.success();
    }

    /**
     * Get hms messages for a single device.
     * @param deviceSn
     * @return
     */
    @GetMapping("/{workspace_id}/devices/hms/{device_sn}")
    public HttpResultResponse<List<DeviceHmsDTO>> getUnreadHmsByDeviceSn(@PathVariable("device_sn") String deviceSn) {
        PaginationData<DeviceHmsDTO> paginationData = deviceHmsService.getDeviceHmsByParam(
                DeviceHmsQueryParam.builder()
                        .deviceSn(new HashSet<>(Set.of(deviceSn)))
                        .updateTime(0L)
                        .build());
        return HttpResultResponse.success(paginationData.getList());
    }
}
