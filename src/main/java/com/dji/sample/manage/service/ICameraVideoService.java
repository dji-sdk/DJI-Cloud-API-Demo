package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.CapacityVideoDTO;
import com.dji.sample.manage.model.receiver.CapacityVideoReceiver;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface ICameraVideoService {

    /**
     * Queries all lens data contained in the camera based on camera id.
     * @param cameraId
     * @return
     */
    List<CapacityVideoDTO> getCameraVideosByCameraId(Integer cameraId);

    /**
     * Deletes all the data of this lens, according to the lens id.
     * @param ids   A collection of lens ids.
     * @return true
     */
    Boolean deleteCameraVideosById(List<Integer> ids);

    /**
     * Save the live capability of all lenses of this camera.
     * @param capacityVideoReceivers live capability of lens
     * @param cameraId
     * @return
     */
    Boolean saveCameraVideoDTOList(List<CapacityVideoReceiver> capacityVideoReceivers, Integer cameraId);

}
