package com.dji.sample.manage.service;

import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sdk.cloudapi.device.ControlSourceEnum;
import com.dji.sdk.cloudapi.device.DeviceOsdHost;
import com.dji.sdk.cloudapi.device.DockModeCodeEnum;
import com.dji.sdk.cloudapi.device.DroneModeCodeEnum;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

/**
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public interface IDeviceService {

    /**
     * The aircraft goes offline.
     * @param deviceSn aircraft's SN
     */
    void subDeviceOffline(String deviceSn);

    /**
     * The gateway goes offline.
     * @param gatewaySn gateway's SN
     */
    void gatewayOffline(String gatewaySn);

    /**
     * Subscribe to the topic of the gateway when the gateway device goes online, and unsubscribe from the topic of the sub-device.
     * @param gateway
     */
    void gatewayOnlineSubscribeTopic(GatewayManager gateway);

    /**
     * Subscribe to the gateway's and sub-device's topics when the drone comes online.
     * @param gateway
     */
    void subDeviceOnlineSubscribeTopic(GatewayManager gateway);

    /**
     * When the gateway device goes offline, unsubscribe from the topics of the gateway and sub-device.
     * @param gateway
     */
    void offlineUnsubscribeTopic(GatewayManager gateway);

    /**
     * Obtain device data according to different query conditions.
     * @param param query parameters
     * @return
     */
    List<DeviceDTO> getDevicesByParams(DeviceQueryParam param);

    /**
     * The business interface on the web side. Get all information about all devices in this workspace.
     * @param workspaceId
     * @return
     */
    List<DeviceDTO> getDevicesTopoForWeb(String workspaceId);

    /**
     * Set the remote controller and payloads information of the drone.
     * @param device
     */
    void spliceDeviceTopo(DeviceDTO device);

    /**
     * Query the information of the device according to the sn of the device.
     * @param sn device sn
     * @return
     */
    Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn);

    /**
     * Convert individual device information into topology objects.
     * @param device
     * @return
     */
    TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device);

    /**
     * When the server receives the request of any device online, offline and topology update in the same workspace,
     * it also broadcasts a push of device online, offline and topology update to PILOT via websocket,
     * and PILOT will get the device topology list again after receiving the push.
     * @param workspaceId
     * @param deviceSn
     */
    void pushDeviceOfflineTopo(String workspaceId, String deviceSn);

    /**
     * When the server receives the request of any device online, offline and topology update in the same workspace,
     * it also broadcasts a push of device online, offline and topology update to PILOT via websocket,
     * and PILOT will get the device topology list again after receiving the push.
     * @param workspaceId
     * @param gatewaySn
     * @param deviceSn
     */
    void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn);

    /**
     * Update the device information.
     * @param deviceDTO
     * @return
     */
    Boolean updateDevice(DeviceDTO deviceDTO);

    /**
     * Bind devices to organizations and people.
     * @param device
     */
    Boolean bindDevice(DeviceDTO device);

    /**
     * Get the binding devices list in one workspace.
     * @param workspaceId
     * @param page
     * @param pageSize
     * @param domain
     * @return
     */
    PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page, Long pageSize, Integer domain);

    /**
     * Unbind device base on device's sn.
     * @param deviceSn
     */
    void unbindDevice(String deviceSn);

    /**
     * Get device information based on device's sn.
     * @param sn device's sn
     * @return device
     */
    Optional<DeviceDTO> getDeviceBySn(String sn);

    /**
     * Create job for device firmware updates.
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    HttpResultResponse createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * Set the property parameters of the drone.
     * @param workspaceId
     * @param dockSn
     * @param param
     * @return
     */
    int devicePropertySet(String workspaceId, String dockSn, JsonNode param);

    /**
     * Check the working status of the dock.
     * @param dockSn
     * @return
     */
    DockModeCodeEnum getDockMode(String dockSn);

    /**
     * Query the working status of the aircraft.
     * @param deviceSn
     * @return
     */
    DroneModeCodeEnum getDeviceMode(String deviceSn);

    /**
     * Check if the dock is in drc mode.
     * @param dockSn
     * @return
     */
    Boolean checkDockDrcMode(String dockSn);

    /**
     * Check if the device has flight control.
     * @param gatewaySn
     * @return
     */
    Boolean checkAuthorityFlight(String gatewaySn);

    Integer saveDevice(DeviceDTO device);

    Boolean saveOrUpdateDevice(DeviceDTO device);

    void pushOsdDataToPilot(String workspaceId, String sn, DeviceOsdHost data);

    void pushOsdDataToWeb(String workspaceId, BizCodeEnum codeEnum, String sn, Object data);

    void updateFlightControl(DeviceDTO gateway, ControlSourceEnum controlSource);
}