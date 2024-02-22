package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.FirmwareModelDTO;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/21
 */
public interface IFirmwareModelService {

    /**
     * Save the relationship between firmware files and device models.
     * @param firmwareModel
     */
    void saveFirmwareDeviceName(FirmwareModelDTO firmwareModel);

}
