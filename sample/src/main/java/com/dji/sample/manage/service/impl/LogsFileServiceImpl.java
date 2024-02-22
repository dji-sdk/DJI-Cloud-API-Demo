package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.manage.dao.ILogsFileMapper;
import com.dji.sample.manage.model.dto.LogsFileDTO;
import com.dji.sample.manage.model.dto.LogsFileUploadDTO;
import com.dji.sample.manage.model.entity.LogsFileEntity;
import com.dji.sample.manage.service.ILogsFileIndexService;
import com.dji.sample.manage.service.ILogsFileService;
import com.dji.sdk.cloudapi.log.FileUploadProgressFile;
import com.dji.sdk.cloudapi.log.FileUploadStartFile;
import com.dji.sdk.cloudapi.log.FileUploadStatusEnum;
import com.dji.sdk.cloudapi.log.LogFileIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Service
@Transactional
public class LogsFileServiceImpl implements ILogsFileService {

    @Autowired
    private ILogsFileMapper mapper;

    @Autowired
    private ILogsFileIndexService logsFileIndexService;

    @Autowired
    private OssServiceContext ossService;

    @Autowired
    private OssServiceContext ossServiceContext;

    @Override
    public List<LogsFileDTO> getLogsFileInfoByLogsId(String logsId) {
        return mapper.selectList(
                new LambdaQueryWrapper<LogsFileEntity>()
                        .eq(LogsFileEntity::getLogsId, logsId)).stream()
                .map(this::entity2Dto).collect(Collectors.toList());
    }

    private LogsFileDTO entity2Dto(LogsFileEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return LogsFileDTO.builder()
                .deviceSn(entity.getDeviceSn())
                .fileId(entity.getFileId())
                .fingerprint(entity.getFingerprint())
                .logsId(entity.getLogsId())
                .name(entity.getName())
                .objectKey(entity.getObjectKey())
                .size(entity.getSize())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public List<LogsFileUploadDTO> getLogsFileByLogsId(String logsId) {
        List<LogsFileDTO> logsFiles = this.getLogsFileInfoByLogsId(logsId);
        if (CollectionUtils.isEmpty(logsFiles)) {
            return new ArrayList<>();
        }
        return logsFileIndexService.getFileIndexByFileIds(logsFiles);
    }

    @Override
    public Boolean insertFile(FileUploadStartFile file, String logsId) {
        LogsFileEntity entity = LogsFileEntity.builder()
                .logsId(logsId)
                .fileId(UUID.randomUUID().toString())
                .objectKey(file.getObjectKey())
                .status(false)
                .deviceSn(file.getDeviceSn())
                .build();
        boolean insert = mapper.insert(entity) > 0;
        if (!insert) {
            return false;
        }
        for (LogFileIndex logsFile : file.getList()) {
            insert = logsFileIndexService.insertFileIndex(logsFile, file.getDeviceSn(), Integer.valueOf(file.getModule().getDomain()), entity.getFileId());
            if (!insert) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void deleteFileByLogsId(String logsId) {
        List<LogsFileDTO> logsFiles = this.getLogsFileInfoByLogsId(logsId);
        if (CollectionUtils.isEmpty(logsFiles)) {
            return;
        }
        mapper.delete(new LambdaUpdateWrapper<LogsFileEntity>().eq(LogsFileEntity::getLogsId, logsId));
        List<String> fileIds = new ArrayList<>();
        logsFiles.forEach(file -> {
            if (file.getStatus()) {
                ossService.deleteObject(OssConfiguration.bucket, file.getObjectKey());
            }
            fileIds.add(file.getFileId());
        });

        logsFileIndexService.deleteFileIndexByFileIds(fileIds);
    }

    @Override
    public void updateFile(String logsId, FileUploadProgressFile fileReceiver) {
        List<LogsFileDTO> logsFiles = this.getLogsFileInfoByLogsId(logsId);
        if (CollectionUtils.isEmpty(logsFiles)) {
            return;
        }
        mapper.update(receiver2Entity(fileReceiver),
                new LambdaUpdateWrapper<LogsFileEntity>().eq(LogsFileEntity::getLogsId, logsId)
                        .eq(LogsFileEntity::getDeviceSn, fileReceiver.getDeviceSn()));
    }

    @Override
    public void updateFileUploadStatus(String logsId, Boolean isUploaded) {
        mapper.update(LogsFileEntity.builder().status(isUploaded).build(),
                new LambdaUpdateWrapper<LogsFileEntity>().eq(LogsFileEntity::getLogsId, logsId));
    }

    @Override
    public URL getLogsFileUrl(String logsId, String fileId) {
        LogsFileEntity logsFile = mapper.selectOne(new LambdaQueryWrapper<LogsFileEntity>()
                .eq(LogsFileEntity::getLogsId, logsId).eq(LogsFileEntity::getFileId, fileId));
        if (Objects.isNull(logsFile)) {
            return null;
        }
        return ossService.getObjectUrl(OssConfiguration.bucket, logsFile.getObjectKey());
    }

    private LogsFileEntity receiver2Entity(FileUploadProgressFile receiver) {
        if (Objects.isNull(receiver)) {
            return null;
        }
        return LogsFileEntity.builder()
                .fingerprint(receiver.getFingerprint())
                .size(receiver.getSize())
                .status(Objects.nonNull(receiver.getProgress())
                        && FileUploadStatusEnum.OK == receiver.getProgress().getStatus())
                .name(receiver.getKey().substring(receiver.getKey().lastIndexOf("/") + 1)).build();
    }
}