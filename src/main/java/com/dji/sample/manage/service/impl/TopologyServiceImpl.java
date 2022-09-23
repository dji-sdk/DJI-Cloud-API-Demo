package com.dji.sample.manage.service.impl;

import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TopologyDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.manage.service.ITopologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Service
public class TopologyServiceImpl implements ITopologyService {

    @Autowired
    private IDeviceService deviceService;

    @Override
    public List<TopologyDTO> getDeviceTopology(String workspaceId) {
        // Query the information of all gateway devices in the workspace.
        List<DeviceDTO> gatewayList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.GATEWAY.getVal()))
                        .build());

        List<TopologyDTO> topologyList = new ArrayList<>();

        gatewayList.forEach(device -> this.getDeviceTopologyByGatewaySn(device.getDeviceSn())
                .ifPresent(topologyList::add));

        return topologyList;
    }

    public Optional<TopologyDTO> getDeviceTopologyByGatewaySn(String gatewaySn) {
        Optional<DeviceDTO> dtoOptional = deviceService.getDeviceBySn(gatewaySn);
        if (dtoOptional.isEmpty()) {
            return Optional.empty();
        }
        List<TopologyDeviceDTO> parents = new ArrayList<>();
        DeviceDTO device = dtoOptional.get();
        TopologyDeviceDTO gateway = deviceService.deviceConvertToTopologyDTO(device);
        parents.add(gateway);

        // Query the topology data of the drone based on the drone sn.
        Optional<TopologyDeviceDTO> deviceTopo = deviceService.getDeviceTopoForPilot(device.getChildDeviceSn());
        List<TopologyDeviceDTO> deviceTopoList = new ArrayList<>();
        deviceTopo.ifPresent(deviceTopoList::add);

        return Optional.ofNullable(TopologyDTO.builder().parents(parents).hosts(deviceTopoList).build());
    }
}
