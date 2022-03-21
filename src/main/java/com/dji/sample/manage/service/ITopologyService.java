package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.TopologyDTO;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
public interface ITopologyService {

    /**
     * Get the topology list of all devices in the workspace for pilot display.
     * @param workspaceId
     * @return
     */
    List<TopologyDTO> getDeviceTopology(String workspaceId);
}
