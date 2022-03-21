package com.dji.sample.manage.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.model.dto.TopologyDTO;
import com.dji.sample.manage.service.ITopologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class TopologyController {

    @Autowired
    private ITopologyService topologyService;

    /**
     * Get the topology list of all devices in the current user workspace for pilot display.
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/devices/topologies")
    public ResponseResult<Map<String, List<TopologyDTO>>> getDevicesTopologiesForPilot(
            @PathVariable(name = "workspace_id") String workspaceId) {
        List<TopologyDTO> topologyList = topologyService.getDeviceTopology(workspaceId);
        return ResponseResult.success(new ConcurrentHashMap<>(Map.of("list", topologyList)));
    }

}
