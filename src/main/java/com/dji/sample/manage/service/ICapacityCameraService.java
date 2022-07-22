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
     * Delete all live capability data for this device based on the device sn.
     * @param deviceSn
     * @return
     */
    Boolean deleteCapacityCameraByDeviceSn(String deviceSn);

    /**
     * Save the live capability data of the device.
     * @param capacityCameraReceivers
     * @param deviceSn
     */
    void saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn);

    /**
     *  Convert the received camera capability object into camera data transfer object.
     * @param receiver
     * @return
     */
    CapacityCameraDTO receiver2Dto(CapacityCameraReceiver receiver);
}
