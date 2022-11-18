package com.dji.sample.control.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.control.model.param.RemoteDebugParam;
import com.dji.sample.control.service.IControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
@RestController
@Slf4j
@RequestMapping("${url.control.prefix}${url.control.version}/devices")
public class DockController {

    @Autowired
    private IControlService controlService;

    @PostMapping("/{sn}/jobs/{service_identifier}")
    public ResponseResult createControlJob(@PathVariable String sn,
                                           @PathVariable("service_identifier") String serviceIdentifier,
                                           @RequestBody(required = false) RemoteDebugParam param) {
        return controlService.controlDock(sn, serviceIdentifier, param);
    }
}
