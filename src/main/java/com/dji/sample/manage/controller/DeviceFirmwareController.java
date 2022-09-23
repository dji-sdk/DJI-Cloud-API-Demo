package com.dji.sample.manage.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.model.dto.DeviceFirmwareNoteDTO;
import com.dji.sample.manage.service.IDeviceFirmwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class DeviceFirmwareController {

    @Autowired
    private IDeviceFirmwareService service;

    /**
     * Get the latest firmware version information for this device model.
     * @param deviceNames
     * @return
     */
    @GetMapping("/firmware-release-notes/latest")
    public ResponseResult<List<DeviceFirmwareNoteDTO>> getLatestFirmwareNote(@RequestParam("device_name") List<String> deviceNames) {
        List<DeviceFirmwareNoteDTO> releaseNotes = new ArrayList<>();
        deviceNames.forEach(deviceName -> {
            Optional<DeviceFirmwareNoteDTO> latestFirmware = service.getLatestFirmwareReleaseNote(deviceName);
            latestFirmware.ifPresent(releaseNotes::add);
        });
        return ResponseResult.success(releaseNotes);
    }

}
