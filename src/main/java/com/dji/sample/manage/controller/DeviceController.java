package com.dji.sample.manage.controller;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.enums.DeviceSetPropertyEnum;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import com.dji.sample.manage.service.IDeviceService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                            .timestamp(System.currentTimeMillis())
                            .method(receiver.getMethod())
                            .build());
        }
    }

    /**
     * Handles the message that the drone goes offline.
     * @param receiver  The drone information is empty.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_OFFLINE, outputChannel = ChannelName.OUTBOUND)
    public void deviceOffline(CommonTopicReceiver<StatusGatewayReceiver> receiver) {

        boolean offline = deviceService.deviceOffline(receiver.getData());
        if (offline) {
            // Notify pilot that the device is offline successfully.
            deviceService.publishStatusReply(receiver.getData().getSn(),
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .timestamp(System.currentTimeMillis())
                            .method(receiver.getMethod())
                            .build());

        }
    }

    /**
     * Get the topology list of all online devices in one workspace.
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/devices")
    public ResponseResult<List<DeviceDTO>> getDevices(@PathVariable("workspace_id") String workspaceId) {
        List<DeviceDTO> devicesList = deviceService.getDevicesTopoForWeb(workspaceId);

        return ResponseResult.success(devicesList);
    }

    /**
     * After binding the device to the workspace, the device data can only be seen on the web.
     * @param device
     * @param deviceSn
     * @return
     */
    @PostMapping("/{device_sn}/binding")
    public ResponseResult bindDevice(@RequestBody DeviceDTO device, @PathVariable("device_sn") String deviceSn) {
        device.setDeviceSn(deviceSn);
        boolean isUpd = deviceService.bindDevice(device);
        return isUpd ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * Obtain device information according to device sn.
     * @param workspaceId
     * @param deviceSn
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}")
    public ResponseResult getDevice(@PathVariable("workspace_id") String workspaceId,
                                               @PathVariable("device_sn") String deviceSn) {
        Optional<DeviceDTO> deviceOpt = deviceService.getDeviceBySn(deviceSn);
        return deviceOpt.isEmpty() ? ResponseResult.error("device not found.") : ResponseResult.success(deviceOpt.get());
    }

    /**
     * Get the binding devices list in one workspace.
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/{workspace_id}/devices/bound")
    public ResponseResult<PaginationData<DeviceDTO>> getBoundDevicesWithDomain(
            @PathVariable("workspace_id") String workspaceId, Integer domain,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(value = "page_size", defaultValue = "50") Long pageSize) {
        PaginationData<DeviceDTO> devices = deviceService.getBoundDevicesWithDomain(workspaceId, page, pageSize, domain);

        return ResponseResult.success(devices);
    }

    /**
     * Removing the binding state of the device.
     * @param deviceSn
     * @return
     */
    @DeleteMapping("/{device_sn}/unbinding")
    public ResponseResult unbindingDevice(@PathVariable("device_sn") String deviceSn) {
        deviceService.unbindDevice(deviceSn);
        return ResponseResult.success();
    }

    /**
     * Update device information.
     * @param device
     * @param workspaceId
     * @param deviceSn
     * @return
     */
    @PutMapping("/{workspace_id}/devices/{device_sn}")
    public ResponseResult updateDevice(@RequestBody DeviceDTO device,
                                       @PathVariable("workspace_id") String workspaceId,
                                       @PathVariable("device_sn") String deviceSn) {
        device.setDeviceSn(deviceSn);
        boolean isUpd = deviceService.updateDevice(device);
        return isUpd ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * Delivers offline firmware upgrade tasks.
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    @PostMapping("/{workspace_id}/devices/ota")
    public ResponseResult createOtaJob(@PathVariable("workspace_id") String workspaceId,
                                       @RequestBody List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        return deviceService.createDeviceOtaJob(workspaceId, upgradeDTOS);
    }

    /**
     * Set the property parameters of the drone.
     * @param workspaceId
     * @param dockSn
     * @param param
     * @return
     */
    @PutMapping("/{workspace_id}/devices/{device_sn}/property")
    public ResponseResult devicePropertySet(@PathVariable("workspace_id") String workspaceId,
                                            @PathVariable("device_sn") String dockSn,
                                            @RequestBody JsonNode param) {
        if (param.size() != 1) {
            return ResponseResult.error(CommonErrorEnum.ILLEGAL_ARGUMENT);
        }
        String property = param.fieldNames().next();
        Optional<DeviceSetPropertyEnum> propertyEnumOpt = DeviceSetPropertyEnum.find(property);
        if (propertyEnumOpt.isEmpty()) {
            return ResponseResult.error(CommonErrorEnum.ILLEGAL_ARGUMENT);
        }
        deviceService.devicePropertySet(workspaceId, dockSn, propertyEnumOpt.get(), param.get(property));
        return ResponseResult.success();
    }
}