package com.dji.sample.manage.service.impl;

import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.manage.model.dto.CapacityCameraDTO;
import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.CapacityCameraReceiver;
import com.dji.sample.manage.service.ICameraVideoService;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Service
//@Transactional
public class CapacityCameraServiceImpl implements ICapacityCameraService {

    @Autowired
    private ICameraVideoService cameraVideoService;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Override
    public List<CapacityCameraDTO> getCapacityCameraByDeviceSn(String deviceSn) {
        return (List<CapacityCameraDTO>) RedisOpsUtils.hashGet(RedisConst.LIVE_CAPACITY, deviceSn);
    }

    @Override
    public Boolean deleteCapacityCameraByDeviceSn(String deviceSn) {
        return RedisOpsUtils.hashDel(RedisConst.LIVE_CAPACITY, new String[]{deviceSn});
    }

    @Override
    public void saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn, Long timestamp) {
        List<CapacityCameraDTO> capacity = capacityCameraReceivers.stream()
                .map(this::receiver2Dto).collect(Collectors.toList());
        RedisOpsUtils.hashSet(RedisConst.LIVE_CAPACITY, deviceSn, capacity);
    }

    @Override
    public CapacityCameraDTO receiver2Dto(CapacityCameraReceiver receiver) {
        CapacityCameraDTO.CapacityCameraDTOBuilder builder = CapacityCameraDTO.builder();
        if (receiver == null) {
            return builder.build();
        }
        int[] indexArr = Arrays.stream(receiver.getCameraIndex().split("-"))
                .map(Integer::valueOf)
                .mapToInt(Integer::intValue)
                .toArray();
        // The cameraIndex consists of type and subType and the index of the payload hanging on the drone.
        // type-subType-index
        if (indexArr.length == 3) {
            Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                    .getOneDictionaryInfoByTypeSubType(DeviceDomainEnum.PAYLOAD.getVal(), indexArr[0], indexArr[1]);
            dictionaryOpt.ifPresent(dictionary ->
                    builder.name(dictionary.getDeviceName()));
        }
        return builder
                .id(UUID.randomUUID().toString())
                .videosList(receiver.getVideosList()
                        .stream()
                        .map(cameraVideoService::receiver2Dto)
                        .collect(Collectors.toList()))
                .index(receiver.getCameraIndex())
                .build();
    }
}