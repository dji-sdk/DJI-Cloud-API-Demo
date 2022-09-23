package com.dji.sample.manage.controller;

import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.manage.model.receiver.DeviceBasicReceiver;
import com.dji.sample.manage.service.IDevicePayloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@RestController
public class DevicePayloadController {

    @Autowired
    private IDevicePayloadService devicePayloadService;

    /**
     * Handles messages in the state topic about basic drone data.
     *
     * Note: Only the data of the drone payload is handled here. You can handle other data from the drone
     * according to your business needs.
     * @param deviceBasic   basic drone data
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_BASIC)
    public void stateBasic(DeviceBasicReceiver deviceBasic, MessageHeaders headers) {

        devicePayloadService.saveDeviceBasicPayload(deviceBasic.getPayloads(), headers.getTimestamp());
    }
}