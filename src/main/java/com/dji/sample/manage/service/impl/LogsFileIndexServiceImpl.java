package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dji.sample.manage.dao.ILogsFileIndexMapper;
import com.dji.sample.manage.model.dto.LogsFileDTO;
import com.dji.sample.manage.model.dto.LogsFileUploadDTO;
import com.dji.sample.manage.model.entity.LogsFileIndexEntity;
import com.dji.sample.manage.service.ILogsFileIndexService;
import com.dji.sdk.cloudapi.log.LogFileIndex;
import com.dji.sdk.cloudapi.log.LogModuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
@Service
@Transactional
public class LogsFileIndexServiceImpl implements ILogsFileIndexService {

    @Autowired
    private ILogsFileIndexMapper mapper;

    @Override
    public Boolean insertFileIndex(LogFileIndex file, String deviceSn, Integer domain, String fileId) {
        LogsFileIndexEntity entity = this.logsFile2Entity(file);
        if (Objects.isNull(entity)) {
            return false;
        }
        entity.setDomain(domain);
        entity.setDeviceSn(deviceSn);
        entity.setFileId(fileId);

        return mapper.insert(entity) > 0;
    }

    @Override
    public List<LogsFileUploadDTO> getFileIndexByFileIds(List<LogsFileDTO> files) {
        List<LogsFileUploadDTO> list = new ArrayList<>();
        files.forEach(file -> {
            Optional<LogsFileUploadDTO> fileOpt = this.getFileIndexByFileId(file.getFileId());
            fileOpt.ifPresent(fileUpload -> {
                fileUpload.setObjectKey(file.getStatus() ? file.getObjectKey() : null);
                list.add(fileUpload);
            });
        });
        return list;
    }

    @Override
    public void deleteFileIndexByFileIds(List<String> fileIds) {
        mapper.delete(new LambdaUpdateWrapper<LogsFileIndexEntity>()
                .or(wrapper -> fileIds.forEach(fileId -> wrapper.eq(LogsFileIndexEntity::getFileId, fileId))));
    }

    @Override
    public Optional<LogsFileUploadDTO> getFileIndexByFileId(String fileId) {
        List<LogsFileIndexEntity> logsFileIndexList = mapper.selectList(
                new LambdaQueryWrapper<LogsFileIndexEntity>().eq(LogsFileIndexEntity::getFileId, fileId));
        if (CollectionUtils.isEmpty(logsFileIndexList)) {
            return Optional.empty();
        }
        LogsFileIndexEntity entity = logsFileIndexList.get(0);
        List<LogFileIndex> logsFileList = logsFileIndexList.stream().map(this::entity2LogsFile).collect(Collectors.toList());
        return Optional.of(LogsFileUploadDTO.builder()
                .deviceSn(entity.getDeviceSn())
                .deviceModelDomain(LogModuleEnum.find(String.valueOf(entity.getDomain())))
                .list(logsFileList)
                .fileId(fileId)
                .build());

    }

    private LogFileIndex entity2LogsFile(LogsFileIndexEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return new LogFileIndex()
                .setBootIndex(entity.getBootIndex())
                .setStartTime(entity.getStartTime())
                .setEndTime(entity.getEndTime())
                .setSize(entity.getSize());

    }

    private LogsFileIndexEntity logsFile2Entity(LogFileIndex file) {
        if (Objects.isNull(file)) {
            return null;
        }
        return LogsFileIndexEntity.builder()
                .bootIndex(file.getBootIndex())
                .size(file.getSize())
                .startTime(file.getStartTime())
                .endTime(file.getEndTime())
                .build();
    }
}
