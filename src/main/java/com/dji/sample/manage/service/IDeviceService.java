package com.dji.sample.manage.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.enums.DeviceModeCodeEnum;
import com.dji.sample.manage.model.enums.DeviceSetPropertyEnum;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.messaging.Message;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public interface IDeviceService {

    /**
     * The device goes online.
     * @param deviceGateway gateway
     * @return Whether the online is successful.
     */
    Boolean deviceOnline(StatusGatewayReceiver deviceGateway);

    /**
     * The device goes offline.
     * @param gateway
     * @return Whether the offline is successful.
     */
    Boolean deviceOffline(StatusGatewayReceiver gateway);

    /**
     * The aircraft goes offline.
     * @param deviceSn aircraft's SN
     * @return Whether the offline is successful.
     */
    Boolean subDeviceOffline(String deviceSn);

    /**
     * When the device goes online, it needs to subscribe to topics.
     * @param sn device's SN
     */
    void subscribeTopicOnline(String sn);

    /**
     * When the device goes offine, it needs to cancel the subscribed topics.
     * @param sn device's SN
     */
    void unsubscribeTopicOffline(String sn);

    /**
     * Delete all device data according to the SN of the device.
     * @param ids device's SN
     * @return
     */
    Boolean delDeviceByDeviceSns(List<String> ids);

    /**
     * Obtain device data according to different query conditions.
     * @param param query parameters
     * @return
     */
    List<DeviceDTO> getDevicesByParams(DeviceQueryParam param);

    /**
     * When you receive a status topic message, you need to reply to it.
     * @param sn   the target of sn
     * @param response
     */
    void publishStatusReply(String sn, CommonTopicResponse<Object> response);

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
     * Push the topology information to the pilot after one device is online.
     * @param sessions  The collection of connection objects on the pilot side.
     * @param sn
     */
    void pushDeviceOnlineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn, String gatewaySn);

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
     * @param sn
     */
    void pushDeviceOfflineTopo(String workspaceId, String sn);

    /**
     * When the server receives the request of any device online, offline and topology update in the same workspace,
     * it also broadcasts a push of device online, offline and topology update to PILOT via websocket,
     * and PILOT will get the device topology list again after receiving the push.
     * @param workspaceId
     * @param deviceSn
     * @param gatewaySn
     */
    void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn);

    /**
     * Handle messages from the osd topic.
     * @param message     osd
     */
    void handleOSD(Message<?> message);

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
    ResponseResult createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * Set the property parameters of the drone.
     * @param workspaceId
     * @param dockSn
     * @param propertyEnum
     * @param param
     */
    void devicePropertySet(String workspaceId, String dockSn, DeviceSetPropertyEnum propertyEnum, JsonNode param);

    /**
     * Set one property parameters of the drone.
     * @param topic
     * @param propertyEnum
     * @param value
     */
    void deviceOnePropertySet(String topic, DeviceSetPropertyEnum propertyEnum, Map.Entry<String, Object> value);

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
    DeviceModeCodeEnum getDeviceMode(String deviceSn);

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

}