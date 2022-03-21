package com.dji.sample.manage.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.AuthInterceptor;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.model.WebSocketManager;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import com.dji.sample.manage.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/devices")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ISendMessageService sendMessageService;

    /**
     * Handles the message that the drone goes online.
     * @param receiver  The drone information is not empty.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_ONLINE, outputChannel = ChannelName.OUTBOUND)
    public void deviceOnline(CommonTopicReceiver<StatusGatewayReceiver> receiver) {
        boolean online = deviceService.deviceOnline(receiver.getData());
        if (online) {
            // Notify pilot that the drone is online successfully.
            deviceService.publishStatusReply(receiver.getData().getSn(),
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .build());

            // Publish the latest device topology information in the current workspace to the pilot.
            deviceService.pushDeviceOnlineTopo(WorkspaceDTO.DEFAULT_WORKSPACE_ID,
                    receiver.getData().getSn(), receiver.getData().getSubDevices().get(0).getSn());
        }
    }

    /**
     * Handles the message that the drone goes offline.
     * @param receiver  The drone information is empty.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_OFFLINE, outputChannel = ChannelName.OUTBOUND)
    public void deviceOffline(CommonTopicReceiver<StatusGatewayReceiver> receiver) {

        boolean offline = deviceService.deviceOffline(receiver.getData().getSn());
        if (offline) {
            // Notify pilot that the device is offline successfully.
            deviceService.publishStatusReply(receiver.getData().getSn(),
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .build());

            // Publish the latest device topology information in the current workspace to the pilot.
            deviceService.pushDeviceOfflineTopo(WorkspaceDTO.DEFAULT_WORKSPACE_ID, receiver.getData().getSn());
        }
    }

    /**
     * Get the topology list of all devices in the current user workspace.
     * @param request
     * @return
     */
    @GetMapping("/devices")
    public ResponseResult<List<DeviceDTO>> getDevices(HttpServletRequest request) {
        // Get information about the current user.
        CustomClaim claim = (CustomClaim)request.getAttribute(AuthInterceptor.TOKEN_CLAIM);
        String workspaceId = claim.getWorkspaceId();
        // Get information about the devices in the current user's workspace.
        List<DeviceDTO> devicesList = deviceService.getDevicesTopoForWeb(workspaceId);

        return ResponseResult.success(devicesList);
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD)
    public void osdRealTime(Message<?> message) {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
        byte[] payload = (byte[])message.getPayload();
        deviceService.handleOSD(topic, payload);
    }

    /**
     * Handles the payloads data of the drone.
     * @param deviceSn  drone's sn
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_PAYLOAD_UPDATE)
    public void pushWebSocketDevices(String deviceSn) {
        List<DeviceDTO> devicesList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(deviceSn)
                        .build());
        // Get drone information based on the sn of the drone. The sn of the drone is unique.
        DeviceDTO device = devicesList.get(0);
        // Set the remote controller and payloads information of the drone.
        deviceService.spliceDeviceTopo(device);

        CustomWebSocketMessage wsMessage = CustomWebSocketMessage.builder()
                .timestamp(System.currentTimeMillis())
                .bizCode(BizCodeEnum.DEVICE_UPDATE_TOPO.getCode())
                .data(device)
                .build();
        // Update the topology of the drone via WebSocket notifications to the web side.
        sendMessageService.sendBatch(WebSocketManager
                .getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                wsMessage);
    }
}