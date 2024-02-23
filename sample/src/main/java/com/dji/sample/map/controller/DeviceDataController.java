package com.dji.sample.map.controller;

import com.dji.sample.map.model.dto.DeviceDataStatusDTO;
import com.dji.sample.map.service.IDeviceDataService;
import com.dji.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */

@RestController
@RequestMapping("${url.map.prefix}${url.map.version}/workspaces")
public class DeviceDataController {

    @Autowired
    private IDeviceDataService deviceDataService;

    @GetMapping("/{workspace_id}/device-status")
    public HttpResultResponse<List<DeviceDataStatusDTO>> getDeviceFlightAreaStatus(@PathVariable(name = "workspace_id") String workspaceId) {
        return HttpResultResponse.success(deviceDataService.getDevicesDataStatus(workspaceId));
    }
}
