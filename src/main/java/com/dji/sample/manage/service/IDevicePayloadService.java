package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.DevicePayloadDTO;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;

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
    Boolean savePayloadDTOs(List<DevicePayloadReceiver> payloadReceiverList);

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
}