package com.dji.sample.map.service;

import com.dji.sample.map.model.dto.DeviceDataStatusDTO;
import com.dji.sample.map.model.dto.DeviceFlightAreaDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */
public interface IDeviceDataService {

    List<DeviceDataStatusDTO> getDevicesDataStatus(String workspaceId);

    Optional<DeviceFlightAreaDTO> getDeviceStatus(String workspaceId, String deviceSn);
}
