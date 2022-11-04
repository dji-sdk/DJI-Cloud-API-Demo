package com.dji.sample.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.wayline.dao.IWaylineJobMapper;
import com.dji.sample.wayline.model.dto.*;
import com.dji.sample.wayline.model.entity.WaylineJobEntity;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.model.enums.WaylineTaskTypeEnum;
import com.dji.sample.wayline.model.param.CreateJobParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Service
@Transactional
@Slf4j
public class WaylineJobServiceImpl implements IWaylineJobService {

    @Autowired
    private IWaylineJobMapper mapper;

    @Autowired
    private IWaylineFileService waylineFileService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private RedisOpsUtils redisOps;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Optional<WaylineJobDTO> createWaylineJob(CreateJobParam param, CustomClaim customClaim) {
        if (Objects.isNull(param)) {
            return Optional.empty();
        }
        // Immediate tasks, allocating time on the backend.
        if (Objects.isNull(param.getExecuteTime())) {
            param.setExecuteTime(System.currentTimeMillis());
        }
        WaylineJobEntity jobEntity = WaylineJobEntity.builder()
                .name(param.getName())
                .dockSn(param.getDockSn())
                .fileId(param.getFileId())
                .username(customClaim.getUsername())
                .workspaceId(customClaim.getWorkspaceId())
                .jobId(UUID.randomUUID().toString())
                .executeTime(param.getExecuteTime())
                .status(WaylineJobStatusEnum.PENDING.getVal())
                .taskType(param.getTaskType())
                .waylineType(param.getWaylineType())
                .build();
        int id = mapper.insert(jobEntity);
        if (id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.entity2Dto(jobEntity));
    }

    @Override
    public ResponseResult publishFlightTask(CreateJobParam param, CustomClaim customClaim) throws SQLException {
        Optional<WaylineJobDTO> waylineJobOpt = this.createWaylineJob(param, customClaim);
        if (waylineJobOpt.isEmpty()) {
            throw new SQLException("Failed to create wayline job.");
        }
        WaylineJobDTO waylineJob = waylineJobOpt.get();

        boolean isOnline = deviceService.checkDeviceOnline(waylineJob.getDockSn());
        if (!isOnline) {
            throw new RuntimeException("Dock is offline.");
        }

        // get wayline file
        Optional<WaylineFileDTO> waylineFile = waylineFileService.getWaylineByWaylineId(waylineJob.getWorkspaceId(), waylineJob.getFileId());
        if (waylineFile.isEmpty()) {
            throw new SQLException("Wayline file doesn't exist.");
        }

        // get file url
        URL url = waylineFileService.getObjectUrl(waylineJob.getWorkspaceId(), waylineFile.get().getWaylineId());

        FlightTaskCreateDTO flightTask = FlightTaskCreateDTO.builder()
                .flightId(waylineJob.getJobId())
                .executeTime(waylineJob.getExecuteTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .taskType(waylineJob.getTaskType())
                .waylineType(waylineJob.getWaylineType())
                .file(FlightTaskFileDTO.builder()
                        .url(url.toString())
                        .fingerprint(waylineFile.get().getSign())
                        .build())
                .build();

        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT +
                waylineJob.getDockSn() + TopicConst.SERVICES_SUF;
        CommonTopicResponse<Object> response = CommonTopicResponse.builder()
                .tid(UUID.randomUUID().toString())
                .bid(waylineJob.getJobId())
                .timestamp(System.currentTimeMillis())
                .data(flightTask)
                .method(ServicesMethodEnum.FLIGHT_TASK_PREPARE.getMethod())
                .build();

        ServiceReply serviceReply = messageSender.publishWithReply(topic, response);
        if (ResponseResult.CODE_SUCCESS == serviceReply.getResult()) {
            log.info("Prepare task ====> Error code: {}", serviceReply.getResult());
            this.updateJob(WaylineJobDTO.builder()
                    .workspaceId(waylineJob.getWorkspaceId())
                    .jobId(waylineJob.getJobId())
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .endTime(LocalDateTime.now())
                    .code(serviceReply.getResult()).build());
            return ResponseResult.error("Prepare task ====> Error code: " + serviceReply.getResult());
        }

        // Issue an immediate task execution command.
        if (WaylineTaskTypeEnum.IMMEDIATE.getVal() == waylineJob.getTaskType()) {
            if (!executeFlightTask(waylineJob.getJobId())) {
                return ResponseResult.error("Failed to execute job.");
            }
        }

        if (WaylineTaskTypeEnum.TIMED.getVal() == waylineJob.getTaskType()) {
            boolean isAdd = redisOps.zAdd(RedisConst.WAYLINE_JOB, waylineJob.getJobId(),
                    waylineJob.getExecuteTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            if (!isAdd) {
                return ResponseResult.error("Failed to create scheduled job.");
            }
        }

        return ResponseResult.success();
    }

    @Override
    public Boolean executeFlightTask(String jobId) {
        // get job
        Optional<WaylineJobDTO> waylineJob = this.getJobByJobId(jobId);
        if (waylineJob.isEmpty()) {
            throw new IllegalArgumentException("Job doesn't exist.");
        }

        boolean isOnline = deviceService.checkDeviceOnline(waylineJob.get().getDockSn());
        if (!isOnline) {
            throw new RuntimeException("Dock is offline.");
        }

        WaylineJobDTO job = waylineJob.get();
        FlightTaskCreateDTO flightTask = FlightTaskCreateDTO.builder().flightId(jobId).build();

        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT +
                job.getDockSn() + TopicConst.SERVICES_SUF;
        CommonTopicResponse<Object> response = CommonTopicResponse.builder()
                .tid(UUID.randomUUID().toString())
                .bid(jobId)
                .timestamp(System.currentTimeMillis())
                .data(flightTask)
                .method(ServicesMethodEnum.FLIGHT_TASK_EXECUTE.getMethod())
                .build();

        ServiceReply serviceReply = messageSender.publishWithReply(topic, response);
        if (serviceReply.getResult() != 0) {
            log.info("Execute job ====> Error code: {}", serviceReply.getResult());
            this.updateJob(WaylineJobDTO.builder()
                    .jobId(jobId)
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .endTime(LocalDateTime.now())
                    .code(serviceReply.getResult()).build());
            return false;
        }

        this.updateJob(WaylineJobDTO.builder()
                .jobId(jobId)
                .status(WaylineJobStatusEnum.IN_PROGRESS.getVal())
                .build());
        redisOps.setWithExpire(jobId,
                EventsReceiver.<FlightTaskProgressReceiver>builder().bid(jobId).sn(job.getDockSn()).build(),
                RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
        return true;
    }

    @Override
    public void cancelFlightTask(String workspaceId, Collection<String> jobIds) {
        List<WaylineJobEntity> waylineJobs = mapper.selectList(
                new LambdaQueryWrapper<WaylineJobEntity>()
                        .or(wrapper -> jobIds.forEach(id -> wrapper.eq(WaylineJobEntity::getJobId, id))));

        // Check if the job have ended.
        List<String> endJobs = waylineJobs.stream()
                .filter(job -> WaylineJobStatusEnum.find(job.getStatus()).getEnd())
                .map(WaylineJobEntity::getName)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(endJobs)) {
            throw new IllegalArgumentException("There are jobs that have ended." + Arrays.toString(endJobs.toArray()));
        }

        Set<String> ids = waylineJobs.stream().map(WaylineJobEntity::getJobId).collect(Collectors.toSet());
        for (String id : jobIds) {
            if (!ids.contains(id)) {
                throw new IllegalArgumentException("Job id " + id + " doesn't exist.");
            }
        }

        // Group job id by dock sn.
        Map<String, List<String>> dockJobs = waylineJobs.stream()
                .collect(Collectors.groupingBy(WaylineJobEntity::getDockSn,
                        Collectors.mapping(WaylineJobEntity::getJobId, Collectors.toList())));
        dockJobs.forEach((dockSn, idList) -> this.publishCancelTask(workspaceId, dockSn, idList));

    }

    private void publishCancelTask(String workspaceId, String dockSn, List<String> jobIds) {
        boolean isOnline = deviceService.checkDeviceOnline(dockSn);
        if (isOnline) {
            throw new RuntimeException("Dock is offline.");
        }
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + dockSn + TopicConst.SERVICES_SUF;

        CommonTopicResponse<Object> response = CommonTopicResponse.builder()
                .tid(UUID.randomUUID().toString())
                .bid(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .data(Map.of(MapKeyConst.FLIGHT_IDS, jobIds))
                .method(ServicesMethodEnum.FLIGHT_TASK_CANCEL.getMethod())
                .build();

        ServiceReply serviceReply = messageSender.publishWithReply(topic, response);
        if (serviceReply.getResult() != 0) {
            log.info("Cancel job ====> Error code: {}", serviceReply.getResult());
            throw new RuntimeException("Failed to cancel the wayline job of " + dockSn);
        }

        for (String jobId : jobIds) {
            this.updateJob(WaylineJobDTO.builder()
                    .workspaceId(workspaceId)
                    .jobId(jobId)
                    .status(WaylineJobStatusEnum.CANCEL.getVal())
                    .endTime(LocalDateTime.now())
                    .build());
            redisOps.zRemove(RedisConst.WAYLINE_JOB, jobId);
        }

    }

    @Override
    public Optional<WaylineJobDTO> getJobByJobId(String jobId) {
        WaylineJobEntity jobEntity = mapper.selectOne(
                new LambdaQueryWrapper<WaylineJobEntity>()
                        .eq(WaylineJobEntity::getJobId, jobId));
        return Optional.ofNullable(entity2Dto(jobEntity));
    }

    @Override
    public Boolean updateJob(WaylineJobDTO dto) {
        return mapper.update(this.dto2Entity(dto),
                new LambdaUpdateWrapper<WaylineJobEntity>()
                        .eq(WaylineJobEntity::getJobId, dto.getJobId())) > 0;
    }

    @Override
    public PaginationData<WaylineJobDTO> getJobsByWorkspaceId(String workspaceId, long page, long pageSize) {
        Page<WaylineJobEntity> pageData = mapper.selectPage(
                new Page<WaylineJobEntity>(page, pageSize),
                new LambdaQueryWrapper<WaylineJobEntity>()
                        .eq(WaylineJobEntity::getWorkspaceId, workspaceId));
        List<WaylineJobDTO> records = pageData.getRecords()
                .stream()
                .map(this::entity2Dto)
                .collect(Collectors.toList());

        return new PaginationData<WaylineJobDTO>(records, new Pagination(pageData));
    }


    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_FLIGHT_TASK_RESOURCE_GET, outputChannel = ChannelName.OUTBOUND)
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void flightTaskResourceGet(CommonTopicReceiver receiver, MessageHeaders headers) {
        Map<String, String> jobIdMap = objectMapper.convertValue(receiver.getData(), new TypeReference<Map<String, String>>() {});
        String jobId = jobIdMap.get(MapKeyConst.FLIGHT_ID);

        CommonTopicResponse.CommonTopicResponseBuilder<RequestsReply> builder = CommonTopicResponse.<RequestsReply>builder()
                .tid(receiver.getTid())
                .bid(receiver.getBid())
                .method(RequestsMethodEnum.FLIGHT_TASK_RESOURCE_GET.getMethod())
                .timestamp(System.currentTimeMillis());

        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString() + TopicConst._REPLY_SUF;

        Optional<WaylineJobDTO> waylineJobOpt = this.getJobByJobId(jobId);
        if (waylineJobOpt.isEmpty()) {
            builder.data(RequestsReply.error(CommonErrorEnum.ILLEGAL_ARGUMENT));
            messageSender.publish(topic, builder.build());
            return;
        }

        WaylineJobDTO waylineJob = waylineJobOpt.get();

        // get wayline file
        Optional<WaylineFileDTO> waylineFile = waylineFileService.getWaylineByWaylineId(waylineJob.getWorkspaceId(), waylineJob.getFileId());
        if (waylineFile.isEmpty()) {
            builder.data(RequestsReply.error(CommonErrorEnum.ILLEGAL_ARGUMENT));
            messageSender.publish(topic, builder.build());
            return;
        }

        // get file url
        URL url = null;
        try {
            url = waylineFileService.getObjectUrl(waylineJob.getWorkspaceId(), waylineFile.get().getWaylineId());
            builder.data(RequestsReply.success(FlightTaskCreateDTO.builder()
                    .file(FlightTaskFileDTO.builder()
                            .url(url.toString())
                            .fingerprint(waylineFile.get().getSign())
                            .build())
                    .build()));

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            builder.data(RequestsReply.error(CommonErrorEnum.ILLEGAL_ARGUMENT));
            messageSender.publish(topic, builder.build());
            return;
        }

        messageSender.publish(topic, builder.build());

    }

    private WaylineJobEntity dto2Entity(WaylineJobDTO dto) {
        WaylineJobEntity.WaylineJobEntityBuilder builder = WaylineJobEntity.builder();
        if (dto == null) {
            return builder.build();
        }
        if (Objects.nonNull(dto.getEndTime())) {
            builder.endTime(dto.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (Objects.nonNull(dto.getExecuteTime())) {
            builder.executeTime(dto.getExecuteTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        return builder.status(dto.getStatus())
                .name(dto.getJobName())
                .errorCode(dto.getCode())
                .build();
    }

    private WaylineJobDTO entity2Dto(WaylineJobEntity entity) {
        if (entity == null) {
            return null;
        }

        WaylineJobDTO.WaylineJobDTOBuilder builder = WaylineJobDTO.builder()
                .jobId(entity.getJobId())
                .jobName(entity.getName())
                .fileId(entity.getFileId())
                .fileName(waylineFileService.getWaylineByWaylineId(entity.getWorkspaceId(), entity.getFileId())
                        .orElse(WaylineFileDTO.builder().build()).getName())
                .dockSn(entity.getDockSn())
                .dockName(deviceService.getDeviceBySn(entity.getDockSn())
                        .orElse(DeviceDTO.builder().build()).getNickname())
                .username(entity.getUsername())
                .workspaceId(entity.getWorkspaceId())
                .status(entity.getStatus())
                .code(entity.getErrorCode())
                .executeTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getExecuteTime()), ZoneId.systemDefault()))
                .taskType(entity.getTaskType())
                .waylineType(entity.getWaylineType());
        if (Objects.nonNull(entity.getEndTime())) {
            builder.endTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getEndTime()), ZoneId.systemDefault()));
        }
        if (WaylineJobStatusEnum.IN_PROGRESS.getVal() == entity.getStatus() && redisOps.getExpire(entity.getJobId()) > 0) {
            EventsReceiver<FlightTaskProgressReceiver> taskProgress = (EventsReceiver<FlightTaskProgressReceiver>) redisOps.get(entity.getJobId());
            if (Objects.nonNull(taskProgress.getOutput()) && Objects.nonNull(taskProgress.getOutput().getProgress())) {
                builder.progress(taskProgress.getOutput().getProgress().getPercent());
            }
        }
        return builder.build();
    }
}
