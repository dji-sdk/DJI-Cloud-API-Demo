package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DevicePayloadDTO;
import com.dji.sample.manage.model.dto.DevicePayloadReceiver;
import com.dji.sdk.cloudapi.device.PayloadFirmwareVersion;

import java.util.Collection;
import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface IDevicePayloadService {

    /**
     * Query if the payload has been saved based on the sn of the payload.
     * @param payloadSn
     * @return
     */
    Integer checkPayloadExist(String payloadSn);

    /**
     * Save all payload data.
     * @param payloadReceiverList
     * @return
     */
    Boolean savePayloadDTOs(DeviceDTO drone, List<DevicePayloadReceiver> payloadReceiverList);

    /**
     * Save a payload data.
     * @param payloadReceiver
     * @return
     */
    Integer saveOnePayloadDTO(DevicePayloadReceiver payloadReceiver);

    /**
     * Query all payload data on this device based on the device sn.
     * @param deviceSn
     * @return
     */
    List<DevicePayloadDTO> getDevicePayloadEntitiesByDeviceSn(String deviceSn);

    /**
     * Delete all payload data on these devices based on the collection of device sns.
     * @param deviceSns
     */
    void deletePayloadsByDeviceSn(List<String> deviceSns);

    /**
     * Update the firmware version information of the payload.
     * @param receiver
     */
    Boolean updateFirmwareVersion(String droneSn, PayloadFirmwareVersion receiver);

    /**
     * Delete payload data based on payload sn.
     * @param payloadSns
     */
    void deletePayloadsByPayloadsSn(Collection<String> payloadSns);

    /**
     * Check if the device has payload control.
     * @param deviceSn
     * @param payloadIndex
     * @return
     */
    Boolean checkAuthorityPayload(String deviceSn, String payloadIndex);

    void updatePayloadControl(DeviceDTO drone, List<DevicePayloadReceiver> payloads);
}