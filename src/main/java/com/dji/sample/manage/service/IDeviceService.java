package com.dji.sample.manage.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.FirmwareVersionReceiver;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import org.springframework.messaging.MessageHeaders;

import java.util.Collection;
import java.util.List;
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
     * @param gatewaySn
     * @return Whether the offline is successful.
     */
    Boolean deviceOffline(String gatewaySn);

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
     * @param topic     osd
     * @param payload
     */
    void handleOSD(String topic, byte[] payload);

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
     * Handle dock binding status requests.
     * Note: If your business does not need to bind the dock to the organization,
     *       you can directly reply to the successful message without implementing business logic.
     * @param receiver
     * @param headers
     */
    void bindStatus(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Handle dock binding requests.
     * Note: If your business does not need to bind the dock to the organization,
     *       you can directly reply to the successful message without implementing business logic.
     * @param receiver
     * @param headers
     */
    void bindDevice(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Get the binding devices list in one workspace.
     * @param workspaceId
     * @param page
     * @param pageSize
     * @param domain
     * @return
     */
    PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page, Long pageSize, String domain);

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
     * Update the firmware version information of the device or payload.
     * @param receiver
     */
    void updateFirmwareVersion(FirmwareVersionReceiver receiver);

    /**
     * Create job for device firmware updates.
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    ResponseResult createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);
}