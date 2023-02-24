package com.dji.sample.manage.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.dto.DeviceFirmwareDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareNoteDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.param.DeviceFirmwareQueryParam;
import com.dji.sample.manage.model.param.DeviceFirmwareUploadParam;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.multipart.MultipartFile;

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
     *
     * @param workspaceId
     * @param deviceName
     * @param version
     * @return
     */
    Optional<DeviceFirmwareDTO> getFirmware(String workspaceId, String deviceName, String version);

    /**
     * Get the latest firmware release note for this device model.
     * @param deviceName
     * @return
     */
    Optional<DeviceFirmwareNoteDTO> getLatestFirmwareReleaseNote(String deviceName);

    /**
     * Get the firmware information that the device needs to update.
     *
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    List<DeviceOtaCreateParam> getDeviceOtaFirmware(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * Interface to handle device firmware update progress.
     * @param receiver
     * @param headers
     */
    void handleOtaProgress(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Query firmware version information by page.
     *
     * @param workspaceId
     * @param param
     * @return
     */
    PaginationData<DeviceFirmwareDTO> getAllFirmwarePagination(String workspaceId, DeviceFirmwareQueryParam param);

    /**
     * Checks for file existence based on md5.
     *
     * @param workspaceId
     * @param fileMd5
     * @return
     */
    Boolean checkFileExist(String workspaceId, String fileMd5);

    /**
     * Import firmware file for device upgrades.
     * @param workspaceId
     * @param creator
     * @param param
     * @param file
     */
    void importFirmwareFile(String workspaceId, String creator, DeviceFirmwareUploadParam param, MultipartFile file);

    /**
     * Save the file information of the firmware.
     * @param firmware
     * @param deviceNames
     */
    void saveFirmwareInfo(DeviceFirmwareDTO firmware, List<String> deviceNames);

    /**
     * Update the file information of the firmware.
     * @param firmware
     */
    void updateFirmwareInfo(DeviceFirmwareDTO firmware);
}
