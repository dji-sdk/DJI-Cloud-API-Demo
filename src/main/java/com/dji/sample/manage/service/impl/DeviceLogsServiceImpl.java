package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.dao.IDeviceLogsMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceLogsEntity;
import com.dji.sample.manage.model.enums.*;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sample.manage.model.param.LogsFileUpdateParam;
import com.dji.sample.manage.model.receiver.*;
import com.dji.sample.manage.service.IDeviceLogsService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.ILogsFileService;
import com.dji.sample.manage.service.ITopologyService;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.dji.sample.storage.service.IStorageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
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
public class DeviceLogsServiceImpl implements IDeviceLogsService {

    private static final String LOGS_FILE_SUFFIX = ".tar";

    @Autowired
    private IDeviceLogsMapper mapper;

    @Autowired
    private ITopologyService topologyService;

    @Autowired
    private IMessageSenderService messageSenderService;

    @Autowired
    private ILogsFileService logsFileService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ISendMessageService webSocketMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

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

        return new PaginationData<DeviceLogsDTO>(deviceLogsList, new Pagination(pagination));
    }

    @Override
    public ResponseResult getRealTimeLogs(String deviceSn, List<String> domainList) {
        boolean exist = deviceRedisService.checkDeviceOnline(deviceSn);
        if (!exist) {
            return ResponseResult.error("Device is offline.");
        }

        ServiceReply<List<LogsFileUpload>> data = messageSenderService.publishServicesTopic(
                new TypeReference<List<LogsFileUpload>>() {}, deviceSn, LogsFileMethodEnum.FILE_UPLOAD_LIST.getMethod(),
                Map.of(MapKeyConst.MODULE_LIST, domainList));

        for (LogsFileUpload file : data.getOutput()) {
            if (file.getDeviceSn().isBlank()) {
                file.setDeviceSn(deviceSn);
            }
        }
        return ResponseResult.success(new LogsFileUploadList(data.getOutput(), data.getResult()));
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
        for (LogsFileUpload file : param.getFiles()) {
            insert = logsFileService.insertFile(file, entity.getLogsId());
            if (!insert) {
                return "";
            }
        }

        return bid;
    }


    @Override
    public ResponseResult pushFileUpload(String username, String deviceSn, DeviceLogsCreateParam param) {
        StsCredentialsDTO stsCredentials = storageService.getSTSCredentials();
        LogsUploadCredentialsDTO credentialsDTO = new LogsUploadCredentialsDTO(stsCredentials);
        // Set the storage name of the file.
        List<LogsFileUpload> files = param.getFiles();
        files.forEach(file -> file.setObjectKey(credentialsDTO.getObjectKeyPrefix() + "/" + UUID.randomUUID().toString() + LOGS_FILE_SUFFIX));

        credentialsDTO.setParams(LogsFileUploadList.builder().files(files).build());
        String bid = UUID.randomUUID().toString();
        ServiceReply reply = messageSenderService.publishServicesTopic(
                deviceSn, LogsFileMethodEnum.FILE_UPLOAD_START.getMethod(), credentialsDTO, bid);

        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            return ResponseResult.error(String.valueOf(reply.getResult()));
        }

        String logsId = this.insertDeviceLogs(bid, username, deviceSn, param);
        if (!bid.equals(logsId)) {
            return ResponseResult.error("Database insert failed.");
        }

        // Save the status of the log upload.
        RedisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + deviceSn, bid, LogsOutputProgressDTO.builder().logsId(logsId).build());
        return ResponseResult.success();

    }

    @Override
    public ResponseResult pushUpdateFile(String deviceSn, LogsFileUpdateParam param) {
        LogsFileUpdateMethodEnum method = LogsFileUpdateMethodEnum.find(param.getStatus());
        if (LogsFileUpdateMethodEnum.UNKNOWN == method) {
            return ResponseResult.error("Illegal param");
        }
        ServiceReply reply = messageSenderService.publishServicesTopic(
                deviceSn, LogsFileMethodEnum.FILE_UPLOAD_UPDATE.getMethod(), param);

        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            return ResponseResult.error("Error Code : " + reply.getResult());
        }

        return ResponseResult.success();
    }

    @Override
    public void deleteLogs(String deviceSn, String logsId) {
        mapper.delete(new LambdaUpdateWrapper<DeviceLogsEntity>()
                .eq(DeviceLogsEntity::getLogsId, logsId).eq(DeviceLogsEntity::getDeviceSn, deviceSn));
        logsFileService.deleteFileByLogsId(logsId);
    }

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @Override
    public CommonTopicReceiver handleFileUploadProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        String sn  = topic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                topic.indexOf(TopicConst.EVENTS_SUF));

        EventsReceiver<OutputLogsProgressReceiver> eventsReceiver = objectMapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<OutputLogsProgressReceiver>>(){});

        EventsReceiver<LogsOutputProgressDTO> webSocketData = new EventsReceiver<>();
        webSocketData.setBid(receiver.getBid());
        webSocketData.setSn(sn);

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(sn);
        if (deviceOpt.isEmpty()) {
            return null;
        }

        DeviceDTO device = deviceOpt.get();

        try {
            OutputLogsProgressReceiver output = eventsReceiver.getOutput();
            EventsResultStatusEnum statusEnum = EventsResultStatusEnum.find(output.getStatus());
            log.info("Logs upload progress: {}", output.toString());

            String key = RedisConst.LOGS_FILE_PREFIX + sn;
            LogsOutputProgressDTO progress;
            boolean exist = RedisOpsUtils.checkExist(key);
            if (!exist && !statusEnum.getEnd()) {
                progress = LogsOutputProgressDTO.builder().logsId(receiver.getBid()).build();
                RedisOpsUtils.hashSet(key, receiver.getBid(), progress);
            } else if (exist) {
                progress = (LogsOutputProgressDTO) RedisOpsUtils.hashGet(key, receiver.getBid());
            } else {
                progress = LogsOutputProgressDTO.builder().build();
            }
            progress.setStatus(output.getStatus());

            // If the logs file is empty, delete the cache of this task.
            List<LogsExtFileReceiver> fileReceivers = output.getExt().getFiles();
            if (CollectionUtils.isEmpty(fileReceivers)) {
                RedisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
            }

            // refresh cache.
            List<LogsProgressDTO> fileProgressList = new ArrayList<>();
            fileReceivers.forEach(file -> {
                LogsProgressReceiver logsProgress = file.getProgress();
                if (!StringUtils.hasText(file.getDeviceSn())) {
                    if (String.valueOf(DeviceDomainEnum.DOCK.getVal()).equals(file.getDeviceModelDomain())) {
                        file.setDeviceSn(sn);
                    } else if (String.valueOf(DeviceDomainEnum.SUB_DEVICE.getVal()).equals(file.getDeviceModelDomain())) {
                        file.setDeviceSn(device.getChildDeviceSn());
                    }
                }

                fileProgressList.add(LogsProgressDTO.builder()
                        .deviceSn(file.getDeviceSn())
                        .deviceModelDomain(file.getDeviceModelDomain())
                        .result(logsProgress.getResult())
                        .status(logsProgress.getStatus())
                        .uploadRate(logsProgress.getUploadRate())
                        .progress(((logsProgress.getCurrentStep() - 1) * 100 + logsProgress.getProgress()) / logsProgress.getTotalStep())
                        .build());
            });
            progress.setFiles(fileProgressList);
            webSocketData.setOutput(progress);
            RedisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + sn, receiver.getBid(), progress);
            // Delete the cache at the end of the task.
            if (statusEnum.getEnd()) {
                RedisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
                this.updateLogsStatus(receiver.getBid(), DeviceLogsStatusEnum.find(statusEnum).getVal());

                fileReceivers.forEach(file -> logsFileService.updateFile(receiver.getBid(), file));
            }
        } catch (NullPointerException e) {
            this.updateLogsStatus(receiver.getBid(), DeviceLogsStatusEnum.FAILED.getVal());

            RedisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
        }

        webSocketMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.FILE_UPLOAD_PROGRESS.getCode(), webSocketData);

        return receiver;
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
                .deviceLogs(LogsFileUploadList.builder().files(logsFileService.getLogsFileByLogsId(entity.getLogsId())).build())
                .logsProgress(Objects.requireNonNullElse(progress, new LogsOutputProgressDTO()).getFiles())
                .deviceTopo(topologyService.getDeviceTopologyByGatewaySn(entity.getDeviceSn()).orElse(null))
                .build();
    }
}
