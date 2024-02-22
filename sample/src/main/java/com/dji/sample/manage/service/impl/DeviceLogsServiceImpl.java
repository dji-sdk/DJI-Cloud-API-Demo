package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.component.mqtt.model.EventsReceiver;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.manage.dao.IDeviceLogsMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceLogsEntity;
import com.dji.sample.manage.model.enums.DeviceLogsStatusEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sample.manage.service.IDeviceLogsService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.ILogsFileService;
import com.dji.sample.manage.service.ITopologyService;
import com.dji.sample.storage.service.IStorageService;
import com.dji.sdk.cloudapi.log.*;
import com.dji.sdk.cloudapi.log.api.AbstractLogService;
import com.dji.sdk.cloudapi.storage.StsCredentialsResponse;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.Pagination;
import com.dji.sdk.common.PaginationData;
import com.dji.sdk.common.SDKManager;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.events.EventsDataRequest;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@Service
@Transactional
@Slf4j
public class DeviceLogsServiceImpl extends AbstractLogService implements IDeviceLogsService {

    private static final String LOGS_FILE_SUFFIX = ".tar";

    @Autowired
    private IDeviceLogsMapper mapper;

    @Autowired
    private ITopologyService topologyService;

    @Autowired
    private ILogsFileService logsFileService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private AbstractLogService abstractLogService;

    @Override
    public PaginationData<DeviceLogsDTO> getUploadedLogs(String deviceSn, DeviceLogsQueryParam param) {
        LambdaQueryWrapper<DeviceLogsEntity> queryWrapper = new LambdaQueryWrapper<DeviceLogsEntity>()
                .eq(DeviceLogsEntity::getDeviceSn, deviceSn)
                .between(Objects.nonNull(param.getBeginTime()) && Objects.nonNull(param.getEndTime()),
                        DeviceLogsEntity::getCreateTime, param.getBeginTime(), param.getEndTime())
                .eq(Objects.nonNull(param.getStatus()), DeviceLogsEntity::getStatus, param.getStatus())
                .like(StringUtils.hasText(param.getLogsInformation()),
                        DeviceLogsEntity::getLogsInfo, param.getLogsInformation())
                .orderByDesc(DeviceLogsEntity::getCreateTime);

        Page<DeviceLogsEntity> pagination = mapper.selectPage(new Page<>(param.getPage(), param.getPageSize()), queryWrapper);

        List<DeviceLogsDTO> deviceLogsList = pagination.getRecords().stream().map(this::entity2Dto).collect(Collectors.toList());

        return new PaginationData<DeviceLogsDTO>(deviceLogsList, new Pagination(pagination.getCurrent(), pagination.getSize(), pagination.getTotal()));
    }

    @Override
    public HttpResultResponse getRealTimeLogs(String deviceSn, List<LogModuleEnum> domainList) {
        boolean exist = deviceRedisService.checkDeviceOnline(deviceSn);
        if (!exist) {
            return HttpResultResponse.error("Device is offline.");
        }

        TopicServicesResponse<ServicesReplyData<FileUploadListResponse>> response = abstractLogService
                .fileuploadList(SDKManager.getDeviceSDK(deviceSn), new FileUploadListRequest().setModuleList(domainList));
        for (FileUploadListFile file : response.getData().getOutput().getFiles()) {
            if (file.getDeviceSn().isBlank()) {
                file.setDeviceSn(deviceSn);
            }
        }
        return HttpResultResponse.success(response.getData().getOutput());
    }

    @Override
    public String insertDeviceLogs(String bid, String username, String deviceSn, DeviceLogsCreateParam param) {
        DeviceLogsEntity entity = DeviceLogsEntity.builder()
                .deviceSn(deviceSn)
                .username(username)
                .happenTime(param.getHappenTime())
                .logsInfo(Objects.requireNonNullElse(param.getLogsInformation(), ""))
                .logsId(bid)
                .status(DeviceLogsStatusEnum.UPLOADING.getVal())
                .build();
        boolean insert = mapper.insert(entity) > 0;
        if (!insert) {
            return "";
        }
        for (FileUploadStartFile file : param.getFiles()) {
            insert = logsFileService.insertFile(file, entity.getLogsId());
            if (!insert) {
                return "";
            }
        }

        return bid;
    }


    @Override
    public HttpResultResponse pushFileUpload(String username, String deviceSn, DeviceLogsCreateParam param) {
        StsCredentialsResponse stsCredentials = storageService.getSTSCredentials();
        stsCredentials.getCredentials().setExpire(System.currentTimeMillis() + (stsCredentials.getCredentials().getExpire() - 60) * 1000);
        LogsUploadCredentialsDTO credentialsDTO = new LogsUploadCredentialsDTO(stsCredentials);
        // Set the storage name of the file.
        List<FileUploadStartFile> files = param.getFiles();
        files.forEach(file -> file.setObjectKey(credentialsDTO.getObjectKeyPrefix() + "/" + UUID.randomUUID().toString() + LOGS_FILE_SUFFIX));

        credentialsDTO.setParams(new FileUploadStartParam().setFiles(files));

        TopicServicesResponse<ServicesReplyData> response = abstractLogService.fileuploadStart(
                SDKManager.getDeviceSDK(deviceSn), new FileUploadStartRequest()
                        .setCredentials(stsCredentials.getCredentials())
                        .setBucket(stsCredentials.getBucket())
                        .setEndpoint(stsCredentials.getEndpoint())
                        .setFileStoreDir(stsCredentials.getObjectKeyPrefix())
                        .setProvider(stsCredentials.getProvider())
                        .setRegion(stsCredentials.getRegion())
                        .setParams(new FileUploadStartParam().setFiles(files)));

        if (!response.getData().getResult().isSuccess()) {
            return HttpResultResponse.error(response.getData().getResult());
        }

        String id = this.insertDeviceLogs(response.getBid(), username, deviceSn, param);

        // Save the status of the log upload.
        RedisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + deviceSn, id, LogsOutputProgressDTO.builder().logsId(id).build());
        return HttpResultResponse.success();

    }

    @Override
    public HttpResultResponse pushUpdateFile(String deviceSn, FileUploadUpdateRequest param) {
        TopicServicesResponse<ServicesReplyData> response = abstractLogService.fileuploadUpdate(SDKManager.getDeviceSDK(deviceSn), param);

        if (!response.getData().getResult().isSuccess()) {
            return HttpResultResponse.error(response.getData().getResult());
        }
        return HttpResultResponse.success();
    }

    @Override
    public void deleteLogs(String deviceSn, String logsId) {
        mapper.delete(new LambdaUpdateWrapper<DeviceLogsEntity>()
                .eq(DeviceLogsEntity::getLogsId, logsId).eq(DeviceLogsEntity::getDeviceSn, deviceSn));
        logsFileService.deleteFileByLogsId(logsId);
    }

    @Override
    public TopicEventsResponse<MqttReply> fileuploadProgress(TopicEventsRequest<EventsDataRequest<FileUploadProgress>> request, MessageHeaders headers) {
        EventsReceiver<LogsOutputProgressDTO> webSocketData = new EventsReceiver<>();
        webSocketData.setBid(request.getBid());
        webSocketData.setSn(request.getGateway());

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(request.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }

        DeviceDTO device = deviceOpt.get();
        String key = RedisConst.LOGS_FILE_PREFIX + request.getGateway();

        try {
            FileUploadProgress output = request.getData().getOutput();
            log.info("Logs upload progress: {}", output.toString());

            LogsOutputProgressDTO progress;
            boolean exist = RedisOpsUtils.checkExist(key);
            if (!exist && !output.getStatus().isEnd()) {
                progress = LogsOutputProgressDTO.builder().logsId(request.getBid()).build();
                RedisOpsUtils.hashSet(key, request.getBid(), progress);
            } else if (exist) {
                progress = (LogsOutputProgressDTO) RedisOpsUtils.hashGet(key, request.getBid());
            } else {
                progress = LogsOutputProgressDTO.builder().build();
            }
            progress.setStatus(output.getStatus());

            // If the logs file is empty, delete the cache of this task.
            List<FileUploadProgressFile> fileReceivers = output.getExt().getFiles();
            if (CollectionUtils.isEmpty(fileReceivers)) {
                RedisOpsUtils.del(key);
            }

            // refresh cache.
            List<LogsProgressDTO> fileProgressList = new ArrayList<>();
            fileReceivers.forEach(file -> {
                LogFileProgress logsProgress = file.getProgress();
                if (!StringUtils.hasText(file.getDeviceSn())) {
                    if (LogModuleEnum.DOCK == file.getModule()) {
                        file.setDeviceSn(request.getGateway());
                    } else if (LogModuleEnum.DRONE == file.getModule()) {
                        file.setDeviceSn(device.getChildDeviceSn());
                    }
                }

                fileProgressList.add(LogsProgressDTO.builder()
                        .deviceSn(file.getDeviceSn())
                        .deviceModelDomain(file.getModule().getDomain())
                        .result(logsProgress.getResult())
                        .status(logsProgress.getStatus().getStatus())
                        .uploadRate(logsProgress.getUploadRate())
                        .progress(((logsProgress.getCurrentStep() - 1) * 100 + logsProgress.getProgress()) / logsProgress.getTotalStep())
                        .build());
            });
            progress.setFiles(fileProgressList);
            webSocketData.setOutput(progress);
            RedisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + request.getGateway(), request.getBid(), progress);
            // Delete the cache at the end of the task.
            if (output.getStatus().isEnd()) {
                RedisOpsUtils.del(key);
                updateLogsStatus(request.getBid(), DeviceLogsStatusEnum.find(output.getStatus()).getVal());

                fileReceivers.forEach(file -> logsFileService.updateFile(request.getBid(), file));
            }
        } catch (NullPointerException e) {
            this.updateLogsStatus(request.getBid(), DeviceLogsStatusEnum.FAILED.getVal());
            RedisOpsUtils.del(key);
        }

        webSocketMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.FILE_UPLOAD_PROGRESS.getCode(), webSocketData);

        return new TopicEventsResponse<MqttReply>().setData(MqttReply.success());
    }

    @Override
    public void updateLogsStatus(String logsId, Integer value) {

        mapper.update(DeviceLogsEntity.builder().status(value).build(),
                new LambdaUpdateWrapper<DeviceLogsEntity>().eq(DeviceLogsEntity::getLogsId, logsId));
        if (DeviceLogsStatusEnum.DONE.getVal() == value) {
            logsFileService.updateFileUploadStatus(logsId, true);
        }
    }

    @Override
    public URL getLogsFileUrl(String logsId, String fileId) {
        return logsFileService.getLogsFileUrl(logsId, fileId);
    }

    private DeviceLogsDTO entity2Dto(DeviceLogsEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        String key = RedisConst.LOGS_FILE_PREFIX + entity.getDeviceSn();
        LogsOutputProgressDTO progress = null;
        if (RedisOpsUtils.hashCheck(key, entity.getLogsId())) {
            progress = (LogsOutputProgressDTO) RedisOpsUtils.hashGet(key, entity.getLogsId());
        }

        return DeviceLogsDTO.builder()
                .logsId(entity.getLogsId())
                .createTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getCreateTime()), ZoneId.systemDefault()))
                .happenTime(Objects.isNull(entity.getHappenTime()) ?
                        null : LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getHappenTime()), ZoneId.systemDefault()))
                .status(entity.getStatus())
                .logsInformation(entity.getLogsInfo())
                .userName(entity.getUsername())
                .deviceLogs(LogsFileUploadListDTO.builder().files(logsFileService.getLogsFileByLogsId(entity.getLogsId())).build())
                .logsProgress(Objects.requireNonNullElse(progress, new LogsOutputProgressDTO()).getFiles())
                .deviceTopo(topologyService.getDeviceTopologyByGatewaySn(entity.getDeviceSn()).orElse(null))
                .build();
    }
}
