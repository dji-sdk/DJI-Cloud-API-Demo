package com.dji.sample.manage.service;

import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TopologyDeviceDTO;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;

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
    void pushDeviceOnlineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn);

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
     * @param gatewaySn
     */
    void pushDeviceOfflineTopo(String workspaceId, String gatewaySn);

    /**
     * When the server receives the request of any device online, offline and topology update in the same workspace,
     * it also broadcasts a push of device online, offline and topology update to PILOT via websocket,
     * and PILOT will get the device topology list again after receiving the push.
     * @param workspaceId
     * @param deviceSn
     * @param gatewaySn
     */
    void pushDeviceOnlineTopo(String workspaceId, String deviceSn, String gatewaySn);

    /**
     * Handle messages from the osd topic.
     * @param topic     osd
     * @param payload
     */
    void handleOSD(String topic, byte[] payload);

}