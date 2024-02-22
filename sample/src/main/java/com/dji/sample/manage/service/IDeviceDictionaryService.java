package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;

import java.util.Optional;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
public interface IDeviceDictionaryService {

    /**
     * Query the type data of the device based on domain, device type and sub type.
     *
     * @param domain
     * @param deviceType
     * @param subType
     * @return
     */
    Optional<DeviceDictionaryDTO> getOneDictionaryInfoByTypeSubType(Integer domain, Integer deviceType, Integer subType);

}
