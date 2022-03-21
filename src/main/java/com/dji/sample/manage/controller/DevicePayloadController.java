package com.dji.sample.manage.controller;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.manage.model.receiver.DeviceBasicReceiver;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;
import com.dji.sample.manage.service.IDevicePayloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@RestController
@Slf4j
public class DevicePayloadController {

    @Autowired
    private IDevicePayloadService devicePayloadService;

    /**
     * Handles the data for the payload messages in the state topic.
     * @param payloadsList  List of payload information.
     * @return  drone's sn
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_PAYLOAD,
            outputChannel = ChannelName.INBOUND_STATE_PAYLOAD_UPDATE)
    public String statePayload(List<DevicePayloadReceiver> payloadsList) {
        // Delete all payload information for the drone based on the drone's sn.
        devicePayloadService.deletePayloadsByDeviceSn(List.of(payloadsList.get(0).getDeviceSn()));

        // Save the new payload information.
        devicePayloadService.savePayloadDTOs(payloadsList);

        log.debug("The result of saving the payload is successful.");

        return payloadsList.get(0).getDeviceSn();
    }

    /**
     * Handles messages in the state topic about basic drone data.
     *
     * Note: Only the data of the drone payload is handled here. You can handle other data from the drone
     * according to your business needs.
     * @param deviceBasic   basic drone data
     * @return  drone's sn
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_BASIC,
            outputChannel = ChannelName.INBOUND_STATE_PAYLOAD_UPDATE)
    public String stateBasic(DeviceBasicReceiver deviceBasic) {
        // Delete all payload information for the drone based on the drone's sn.
        devicePayloadService.deletePayloadsByDeviceSn(List.of(deviceBasic.getDeviceSn()));

        // Save the new payload information.
        boolean isSave = devicePayloadService.savePayloadDTOs(deviceBasic.getPayloads());

        log.debug("The result of saving the payloads is {}.", isSave);

        return deviceBasic.getDeviceSn();
    }
}