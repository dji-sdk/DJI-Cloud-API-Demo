package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.CapacityCameraDTO;
import com.dji.sample.manage.model.receiver.CapacityCameraReceiver;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface ICapacityCameraService {

    /**
     * Query all camera data that can be live streamed from this device based on the device sn.
     * @param deviceSn
     * @return
     */
    List<CapacityCameraDTO> getCapacityCameraByDeviceSn(String deviceSn);

    /**
     * Query whether this camera data has been saved based on the device sn and camera location.
     * @param deviceSn
     * @param cameraIndex
     * @return
     */
    Boolean checkExist(String deviceSn, String cameraIndex);

    /**
     * Delete all live capability data for this device based on the device sn.
     * @param deviceSn
     * @return
     */
    Boolean deleteCapacityCameraByDeviceSn(String deviceSn);

    /**
     * Save the live capability data of the device.
     * @param capacityCameraReceivers
     * @param deviceSn
     * @return
     */
    Boolean saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn);

}
