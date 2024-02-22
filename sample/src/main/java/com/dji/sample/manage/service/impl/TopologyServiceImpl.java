package com.dji.sample.manage.service.impl;

import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.manage.service.ITopologyService;
import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.cloudapi.tsa.DeviceTopology;
import com.dji.sdk.cloudapi.tsa.TopologyList;
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
    public List<TopologyList> getDeviceTopology(String workspaceId) {
        // Query the information of all gateway devices in the workspace.
        List<DeviceDTO> gatewayList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.REMOTER_CONTROL.getDomain()))
                        .build());

        List<TopologyList> topologyList = new ArrayList<>();

        gatewayList.forEach(device -> this.getDeviceTopologyByGatewaySn(device.getDeviceSn())
                .ifPresent(topologyList::add));

        return topologyList;
    }

    public Optional<TopologyList> getDeviceTopologyByGatewaySn(String gatewaySn) {
        Optional<DeviceDTO> dtoOptional = deviceService.getDeviceBySn(gatewaySn);
        if (dtoOptional.isEmpty()) {
            return Optional.empty();
        }
        List<DeviceTopology> parents = new ArrayList<>();
        DeviceDTO device = dtoOptional.get();
        DeviceTopology gateway = deviceService.deviceConvertToTopologyDTO(device);
        parents.add(gateway);

        // Query the topology data of the drone based on the drone sn.
        Optional<TopologyDeviceDTO> deviceTopo = deviceService.getDeviceTopoForPilot(device.getChildDeviceSn());
        List<DeviceTopology> deviceTopoList = new ArrayList<>();
        deviceTopo.ifPresent(deviceTopoList::add);

        return Optional.ofNullable(new TopologyList().setParents(parents).setHosts(deviceTopoList));
    }

}
