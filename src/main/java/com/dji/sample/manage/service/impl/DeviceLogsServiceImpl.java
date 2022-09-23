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
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.manage.dao.IDeviceLogsMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceLogsEntity;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.enums.DeviceLogsStatusEnum;
import com.dji.sample.manage.model.enums.LogsFileUpdateMethodEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceLogsCreateParam;
import com.dji.sample.manage.model.param.DeviceLogsQueryParam;
import com.dji.sample.manage.model.param.LogsFileUpdateParam;
import com.dji.sample.manage.model.receiver.*;
import com.dji.sample.manage.service.IDeviceLogsService;
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
    private RedisOpsUtils redisOpsUtils;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ISendMessageService webSocketMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

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
        boolean exist = redisOpsUtils.getExpire(RedisConst.DEVICE_ONLINE_PREFIX + deviceSn) > 0;
        if (!exist) {
            return ResponseResult.error("Device is offline.");
        }

        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + deviceSn + TopicConst.SERVICES_SUF;
        Optional<LogsFileUploadList> serviceReplyOpt = messageSenderService.publishWithReply(
                LogsFileUploadList.class,
                topic,
                CommonTopicResponse.builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(UUID.randomUUID().toString())
                        .method(ServicesMethodEnum.FILE_UPLOAD_LIST.getMethod())
                        .timestamp(System.currentTimeMillis())
                        .data(Map.of(MapKeyConst.MODULE_LIST, domainList))
                        .build(), 1);
        if (serviceReplyOpt.isEmpty()) {
            return ResponseResult.error("No message reply received.");
        }
        LogsFileUploadList data = serviceReplyOpt.get();
        for (LogsFileUpload file : data.getFiles()) {
            if (file.getDeviceSn().isBlank()) {
                file.setDeviceSn(deviceSn);
            }
        }
        return ResponseResult.success(data);
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
        Optional<ServiceReply> serviceReply = messageSenderService.publishWithReply(
                TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + deviceSn + TopicConst.SERVICES_SUF,
                CommonTopicResponse.<LogsUploadCredentialsDTO>builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(bid)
                        .timestamp(System.currentTimeMillis())
                        .method(ServicesMethodEnum.FILE_UPLOAD_START.getMethod())
                        .data(credentialsDTO)
                        .build());

        if (serviceReply.isEmpty()) {
            return ResponseResult.error("No message reply received.");
        }
        ServiceReply reply = serviceReply.get();
        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            return ResponseResult.error(String.valueOf(reply.getResult()));
        }

        String logsId = this.insertDeviceLogs(bid, username, deviceSn, param);
        if (!bid.equals(logsId)) {
            return ResponseResult.error("Database insert failed.");
        }

        // Save the status of the log upload.
        redisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + deviceSn, bid, LogsOutputProgressDTO.builder().logsId(logsId).build());
        return ResponseResult.success();

    }

    @Override
    public ResponseResult pushUpdateFile(String deviceSn, LogsFileUpdateParam param) {
        LogsFileUpdateMethodEnum method = LogsFileUpdateMethodEnum.find(param.getStatus());
        if (LogsFileUpdateMethodEnum.UNKNOWN == method) {
            return ResponseResult.error("Illegal param");
        }
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + deviceSn + TopicConst.SERVICES_SUF;
        String bid = UUID.randomUUID().toString();
        Optional<ServiceReply> serviceReply = messageSenderService.publishWithReply(topic,
                CommonTopicResponse.<LogsFileUpdateParam>builder()
                        .tid(UUID.randomUUID().toString())
                        .bid(bid)
                        .timestamp(System.currentTimeMillis())
                        .method(ServicesMethodEnum.FILE_UPLOAD_UPDATE.getMethod())
                        .data(param)
                        .build());

        if (serviceReply.isEmpty()) {
            return ResponseResult.error("No message reply received.");
        }
        ServiceReply reply = serviceReply.get();
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

    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILE_UPLOAD_PROGRESS, outputChannel = ChannelName.OUTBOUND)
    @Override
    public void handleFileUploadProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        String sn  = topic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                topic.indexOf(TopicConst.EVENTS_SUF));

        if (receiver.getNeedReply() != null && receiver.getNeedReply() == 1) {
            String replyTopic = headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF;
            messageSenderService.publish(replyTopic,
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .method(receiver.getMethod())
                            .timestamp(System.currentTimeMillis())
                            .data(ResponseResult.success())
                            .build());
        }

        EventsReceiver<OutputLogsProgressReceiver> eventsReceiver = objectMapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<OutputLogsProgressReceiver>>(){});

        EventsReceiver<LogsOutputProgressDTO> webSocketData = new EventsReceiver<>();
        webSocketData.setBid(receiver.getBid());
        webSocketData.setSn(sn);

        DeviceDTO device = (DeviceDTO) redisOpsUtils.get(RedisConst.DEVICE_ONLINE_PREFIX + sn);

        try {
            OutputLogsProgressReceiver output = eventsReceiver.getOutput();
            EventsResultStatusEnum statusEnum = EventsResultStatusEnum.find(output.getStatus());
            log.info("Logs upload progress: {}", output.toString());

            String key = RedisConst.LOGS_FILE_PREFIX + sn;
            LogsOutputProgressDTO progress;
            boolean exist = redisOpsUtils.checkExist(key);
            if (!exist && !statusEnum.getEnd()) {
                progress = LogsOutputProgressDTO.builder().logsId(receiver.getBid()).build();
                redisOpsUtils.hashSet(key, receiver.getBid(), progress);
            } else if (exist) {
                progress = (LogsOutputProgressDTO) redisOpsUtils.hashGet(key, receiver.getBid());
            } else {
                progress = LogsOutputProgressDTO.builder().build();
            }
            progress.setStatus(output.getStatus());

            // If the logs file is empty, delete the cache of this task.
            List<LogsExtFileReceiver> fileReceivers = output.getExt().getFiles();
            if (CollectionUtils.isEmpty(fileReceivers)) {
                redisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
                return;
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
            redisOpsUtils.hashSet(RedisConst.LOGS_FILE_PREFIX + sn, receiver.getBid(), progress);
            // Delete the cache at the end of the task.
            if (statusEnum.getEnd()) {
                redisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
                this.updateLogsStatus(receiver.getBid(), DeviceLogsStatusEnum.find(statusEnum).getVal());

                fileReceivers.forEach(file -> logsFileService.updateFile(receiver.getBid(), file));
            }
        } catch (NullPointerException e) {
            this.updateLogsStatus(receiver.getBid(), DeviceLogsStatusEnum.FAILED.getVal());

            redisOpsUtils.del(RedisConst.LOGS_FILE_PREFIX + sn);
        }

        webSocketMessageService.sendBatch(
                webSocketManageService.getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                CustomWebSocketMessage.builder()
                        .data(webSocketData)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(receiver.getMethod())
                        .build());

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
        LogsOutputProgressDTO progress = new LogsOutputProgressDTO();
        if (redisOpsUtils.hashCheck(key, entity.getLogsId())) {
            progress = (LogsOutputProgressDTO) redisOpsUtils.hashGet(key, entity.getLogsId());
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
                .logsProgress(progress.getFiles())
                .deviceTopo(topologyService.getDeviceTopologyByGatewaySn(entity.getDeviceSn()).orElse(null))
                .build();
    }
}
