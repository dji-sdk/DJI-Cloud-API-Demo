package com.dji.sample.manage.service;

import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.dto.DeviceFirmwareDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareNoteDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import org.springframework.messaging.MessageHeaders;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
public interface IDeviceFirmwareService {

    /**
     * Query specific firmware information based on the device model and firmware version.
     * @param deviceName
     * @param version
     * @return
     */
    Optional<DeviceFirmwareDTO> getFirmware(String deviceName, String version);

    /**
     * Get the latest firmware release note for this device model.
     * @param deviceName
     * @return
     */
    Optional<DeviceFirmwareNoteDTO> getLatestFirmwareReleaseNote(String deviceName);

    /**
     * Get the firmware information that the device needs to update.
     * @param upgradeDTOS
     * @return
     */
    List<DeviceOtaCreateParam> getDeviceOtaFirmware(List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * Interface to handle device firmware update progress.
     * @param receiver
     * @param headers
     */
    void handleOtaProgress(CommonTopicReceiver receiver, MessageHeaders headers);
}
