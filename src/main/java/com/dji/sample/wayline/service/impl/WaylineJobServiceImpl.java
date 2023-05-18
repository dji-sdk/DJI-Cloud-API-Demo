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
import com.dji.sample.control.model.param.DrcModeParam;
import com.dji.sample.control.service.IDrcService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.DeviceModeCodeEnum;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.model.receiver.OsdDockReceiver;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.media.model.MediaFileCountDTO;
import com.dji.sample.media.model.MediaMethodEnum;
import com.dji.sample.media.service.IFileService;
import com.dji.sample.wayline.dao.IWaylineJobMapper;
import com.dji.sample.wayline.model.dto.*;
import com.dji.sample.wayline.model.entity.WaylineJobEntity;
import com.dji.sample.wayline.model.enums.*;
import com.dji.sample.wayline.model.param.CreateJobParam;
import com.dji.sample.wayline.model.param.UpdateJobParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.dji.sample.wayline.service.IWaylineRedisService;
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
import org.springframework.util.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
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
    private ObjectMapper objectMapper;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IDrcService drcService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IWaylineRedisService waylineRedisService;

    private Optional<WaylineJobDTO> insertWaylineJob(WaylineJobEntity jobEntity) {
        int id = mapper.insert(jobEntity);
        if (id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.entity2Dto(jobEntity));
    }

    @Override
    public Optional<WaylineJobDTO> createWaylineJob(CreateJobParam param, String workspaceId, String username, Long beginTime, Long endTime) {
        if (Objects.isNull(param)) {
            return Optional.empty();
        }
        // Immediate tasks, allocating time on the backend.
        WaylineJobEntity jobEntity = WaylineJobEntity.builder()
                .name(param.getName())
                .dockSn(param.getDockSn())
                .fileId(param.getFileId())
                .username(username)
                .workspaceId(workspaceId)
                .jobId(UUID.randomUUID().toString())
                .beginTime(beginTime)
                .endTime(endTime)
                .status(WaylineJobStatusEnum.PENDING.getVal())
                .taskType(param.getTaskType().getVal())
                .waylineType(param.getWaylineType().getVal())
                .outOfControlAction(param.getOutOfControlAction())
                .rthAltitude(param.getRthAltitude())
                .mediaCount(0)
                .build();

        return insertWaylineJob(jobEntity);
    }

    @Override
    public Optional<WaylineJobDTO> createWaylineJobByParent(String workspaceId, String parentId) {
        Optional<WaylineJobDTO> parentJobOpt = this.getJobByJobId(workspaceId, parentId);
        if (parentJobOpt.isEmpty()) {
            return Optional.empty();
        }
        WaylineJobEntity jobEntity = this.dto2Entity(parentJobOpt.get());
        jobEntity.setJobId(UUID.randomUUID().toString());
        jobEntity.setErrorCode(null);
        jobEntity.setCompletedTime(null);
        jobEntity.setExecuteTime(null);
        jobEntity.setStatus(WaylineJobStatusEnum.PENDING.getVal());
        jobEntity.setParentId(parentId);

        return this.insertWaylineJob(jobEntity);
    }

    /**
     * For immediate tasks, the server time shall prevail.
     * @param param
     */
    private void fillImmediateTime(CreateJobParam param) {
        if (WaylineTaskTypeEnum.IMMEDIATE != param.getTaskType()) {
            return;
        }
        long now = System.currentTimeMillis() / 1000;
        param.setTaskDays(Collections.singletonList(now));
        param.setTaskPeriods(Collections.singletonList(Collections.singletonList(now)));
    }

    @Override
    public ResponseResult publishFlightTask(CreateJobParam param, CustomClaim customClaim) throws SQLException {
        fillImmediateTime(param);

        param.getTaskDays().sort((a, b) -> (int) (a - b));
        param.getTaskPeriods().sort((a, b) -> (int) (a.get(0) - b.get(0)));
        for (Long taskDay : param.getTaskDays()) {
            LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(taskDay), ZoneId.systemDefault());
            for (List<Long> taskPeriod : param.getTaskPeriods()) {
                long beginTime = LocalDateTime.of(date, LocalTime.ofInstant(Instant.ofEpochSecond(taskPeriod.get(0)), ZoneId.systemDefault()))
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endTime = taskPeriod.size() > 1 && Objects.nonNull(taskPeriod.get(1)) ?
                        LocalDateTime.of(date, LocalTime.ofInstant(Instant.ofEpochSecond(taskPeriod.get(1)), ZoneId.systemDefault()))
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : beginTime;
                if (WaylineTaskTypeEnum.IMMEDIATE != param.getTaskType() && endTime < System.currentTimeMillis()) {
                    return ResponseResult.error("The task has expired.");
                }
                Optional<WaylineJobDTO> waylineJobOpt = this.createWaylineJob(param, customClaim.getWorkspaceId(), customClaim.getUsername(), beginTime, endTime);
                if (waylineJobOpt.isEmpty()) {
                    return ResponseResult.error("Failed to create wayline job.");
                }

                WaylineJobDTO waylineJob = waylineJobOpt.get();
                if (WaylineTaskTypeEnum.IMMEDIATE == param.getTaskType()) {
                    return this.publishOneFlightTask(waylineJob);
                }

                // If it is a conditional task type, add conditions to the job parameters.
                addPreparedJob(waylineJob, param, beginTime, endTime);
            }
        }
        return ResponseResult.success();
    }

    private void addPreparedJob(WaylineJobDTO waylineJob, CreateJobParam param, Long beginTime, Long endTime) {
        if (WaylineTaskTypeEnum.CONDITION == param.getTaskType()) {
            waylineJob.setConditions(
                    WaylineTaskConditionDTO.builder()
                            .executableConditions(Objects.nonNull(param.getMinStorageCapacity()) ?
                                    WaylineTaskExecutableConditionDTO.builder().storageCapacity(param.getMinStorageCapacity()).build() : null)
                            .readyConditions(WaylineTaskReadyConditionDTO.builder()
                                    .batteryCapacity(param.getMinBatteryCapacity())
                                    .beginTime(beginTime)
                                    .endTime(endTime)
                                    .build())
                            .build());

            waylineRedisService.setConditionalWaylineJob(waylineJob);
        }
        // value: {workspace_id}:{dock_sn}:{job_id}
        boolean isAdd = waylineRedisService.addPreparedWaylineJob(waylineJob);
        if (!isAdd) {
            throw new RuntimeException("Failed to create prepare job.");
        }
    }

    public ResponseResult publishOneFlightTask(WaylineJobDTO waylineJob) throws SQLException {

        boolean isSuccess = this.prepareFlightTask(waylineJob);
        if (!isSuccess) {
            return ResponseResult.error("Failed to prepare job.");
        }

        // Issue an immediate task execution command.
        if (WaylineTaskTypeEnum.IMMEDIATE == waylineJob.getTaskType()) {
            boolean isExecuted = executeFlightTask(waylineJob.getWorkspaceId(), waylineJob.getJobId());
            if (!isExecuted) {
                return ResponseResult.error("Failed to execute job.");
            }
        }

        return ResponseResult.success();
    }

    private Boolean prepareFlightTask(WaylineJobDTO waylineJob) throws SQLException {

        boolean isOnline = deviceRedisService.checkDeviceOnline(waylineJob.getDockSn());
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

        WaylineTaskCreateDTO flightTask = WaylineTaskCreateDTO.builder()
                .flightId(waylineJob.getJobId())
                .executeTime(waylineJob.getBeginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .taskType(waylineJob.getTaskType())
                .waylineType(waylineJob.getWaylineType())
                .rthAltitude(waylineJob.getRthAltitude())
                .outOfControlAction(waylineJob.getOutOfControlAction())
                .file(WaylineTaskFileDTO.builder()
                        .url(url.toString())
                        .fingerprint(waylineFile.get().getSign())
                        .build())
                .build();

        if (WaylineTaskTypeEnum.CONDITION == waylineJob.getTaskType()) {
            if (Objects.isNull(waylineJob.getConditions())) {
                throw new IllegalArgumentException();
            }
            flightTask.setReadyConditions(waylineJob.getConditions().getReadyConditions());
            flightTask.setExecutableConditions(waylineJob.getConditions().getExecutableConditions());
        }

        ServiceReply serviceReply = messageSender.publishServicesTopic(
                waylineJob.getDockSn(), WaylineMethodEnum.FLIGHT_TASK_PREPARE.getMethod(), flightTask, waylineJob.getJobId());
        if (ResponseResult.CODE_SUCCESS != serviceReply.getResult()) {
            log.info("Prepare task ====> Error code: {}", serviceReply.getResult());
            this.updateJob(WaylineJobDTO.builder()
                    .workspaceId(waylineJob.getWorkspaceId())
                    .jobId(waylineJob.getJobId())
                    .executeTime(LocalDateTime.now())
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .completedTime(LocalDateTime.now())
                    .code(serviceReply.getResult()).build());
            return false;
        }
        return true;
    }

    @Override
    public Boolean executeFlightTask(String workspaceId, String jobId) {
        // get job
        Optional<WaylineJobDTO> waylineJob = this.getJobByJobId(workspaceId, jobId);
        if (waylineJob.isEmpty()) {
            throw new IllegalArgumentException("Job doesn't exist.");
        }

        boolean isOnline = deviceRedisService.checkDeviceOnline(waylineJob.get().getDockSn());
        if (!isOnline) {
            throw new RuntimeException("Dock is offline.");
        }

        WaylineJobDTO job = waylineJob.get();
        WaylineTaskCreateDTO flightTask = WaylineTaskCreateDTO.builder().flightId(jobId).build();

        ServiceReply serviceReply = messageSender.publishServicesTopic(
                job.getDockSn(), WaylineMethodEnum.FLIGHT_TASK_EXECUTE.getMethod(), flightTask, jobId);
        if (ResponseResult.CODE_SUCCESS != serviceReply.getResult()) {
            log.info("Execute job ====> Error code: {}", serviceReply.getResult());
            this.updateJob(WaylineJobDTO.builder()
                    .jobId(jobId)
                    .executeTime(LocalDateTime.now())
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .completedTime(LocalDateTime.now())
                    .code(serviceReply.getResult()).build());
            // The conditional task fails and enters the blocking status.
            if (WaylineTaskTypeEnum.CONDITION == job.getTaskType()
                    && WaylineErrorCodeEnum.find(serviceReply.getResult()).isBlock()) {
                waylineRedisService.setBlockedWaylineJob(job.getDockSn(), jobId);
            }
            return false;
        }

        this.updateJob(WaylineJobDTO.builder()
                .jobId(jobId)
                .executeTime(LocalDateTime.now())
                .status(WaylineJobStatusEnum.IN_PROGRESS.getVal())
                .build());
        waylineRedisService.setRunningWaylineJob(job.getDockSn(), EventsReceiver.<WaylineTaskProgressReceiver>builder().bid(jobId).sn(job.getDockSn()).build());
        return true;
    }

    @Override
    public void cancelFlightTask(String workspaceId, Collection<String> jobIds) {
        List<WaylineJobDTO> waylineJobs = getJobsByConditions(workspaceId, jobIds, WaylineJobStatusEnum.PENDING);

        Set<String> waylineJobIds = waylineJobs.stream().map(WaylineJobDTO::getJobId).collect(Collectors.toSet());
        // Check if the task status is correct.
        boolean isErr = !jobIds.removeAll(waylineJobIds) || !jobIds.isEmpty() ;
        if (isErr) {
            throw new IllegalArgumentException("These tasks have an incorrect status and cannot be canceled. " + Arrays.toString(jobIds.toArray()));
        }

        // Group job id by dock sn.
        Map<String, List<String>> dockJobs = waylineJobs.stream()
                .collect(Collectors.groupingBy(WaylineJobDTO::getDockSn,
                        Collectors.mapping(WaylineJobDTO::getJobId, Collectors.toList())));
        dockJobs.forEach((dockSn, idList) -> this.publishCancelTask(workspaceId, dockSn, idList));

    }

    public void publishCancelTask(String workspaceId, String dockSn, List<String> jobIds) {
        boolean isOnline = deviceRedisService.checkDeviceOnline(dockSn);
        if (!isOnline) {
            throw new RuntimeException("Dock is offline.");
        }

        ServiceReply serviceReply = messageSender.publishServicesTopic(
                dockSn, WaylineMethodEnum.FLIGHT_TASK_CANCEL.getMethod(), Map.of(MapKeyConst.FLIGHT_IDS, jobIds));
        if (ResponseResult.CODE_SUCCESS != serviceReply.getResult()) {
            log.info("Cancel job ====> Error code: {}", serviceReply.getResult());
            throw new RuntimeException("Failed to cancel the wayline job of " + dockSn);
        }

        for (String jobId : jobIds) {
            this.updateJob(WaylineJobDTO.builder()
                    .workspaceId(workspaceId)
                    .jobId(jobId)
                    .status(WaylineJobStatusEnum.CANCEL.getVal())
                    .completedTime(LocalDateTime.now())
                    .build());
        }

    }

    public List<WaylineJobDTO> getJobsByConditions(String workspaceId, Collection<String> jobIds, WaylineJobStatusEnum status) {
        return mapper.selectList(
                new LambdaQueryWrapper<WaylineJobEntity>()
                        .eq(WaylineJobEntity::getWorkspaceId, workspaceId)
                        .eq(Objects.nonNull(status), WaylineJobEntity::getStatus, status.getVal())
                        .in(!CollectionUtils.isEmpty(jobIds), WaylineJobEntity::getJobId, jobIds))
                .stream()
                .map(this::entity2Dto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WaylineJobDTO> getJobByJobId(String workspaceId, String jobId) {
        WaylineJobEntity jobEntity = mapper.selectOne(
                new LambdaQueryWrapper<WaylineJobEntity>()
                        .eq(WaylineJobEntity::getWorkspaceId, workspaceId)
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
                        .eq(WaylineJobEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(WaylineJobEntity::getId));
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

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(receiver.getGateway());
        if (deviceOpt.isEmpty()) {
            return;
        }
        Optional<WaylineJobDTO> waylineJobOpt = this.getJobByJobId(deviceOpt.get().getWorkspaceId(), jobId);
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
            builder.data(RequestsReply.success(WaylineTaskCreateDTO.builder()
                    .file(WaylineTaskFileDTO.builder()
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

    @Override
    public void uploadMediaHighestPriority(String workspaceId, String jobId) {
        Optional<WaylineJobDTO> jobOpt = getJobByJobId(workspaceId, jobId);
        if (jobOpt.isEmpty()) {
            throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getErrorMsg());
        }

        String dockSn = jobOpt.get().getDockSn();
        String key = RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + dockSn;
        if (RedisOpsUtils.checkExist(key) && jobId.equals(((MediaFileCountDTO) RedisOpsUtils.get(key)).getJobId())) {
            return;
        }

        ServiceReply reply = messageSender.publishServicesTopic(
                dockSn, MediaMethodEnum.UPLOAD_FLIGHT_TASK_MEDIA_PRIORITIZE.getMethod(), Map.of(MapKeyConst.FLIGHT_ID, jobId));
        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            throw new RuntimeException("Failed to set media job upload priority. Error Code: " + reply.getResult());
        }
    }

    private WaylineJobEntity dto2Entity(WaylineJobDTO dto) {
        WaylineJobEntity.WaylineJobEntityBuilder builder = WaylineJobEntity.builder();
        if (dto == null) {
            return builder.build();
        }
        if (Objects.nonNull(dto.getBeginTime())) {
            builder.beginTime(dto.getBeginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (Objects.nonNull(dto.getEndTime())) {
            builder.endTime(dto.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (Objects.nonNull(dto.getExecuteTime())) {
            builder.executeTime(dto.getExecuteTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (Objects.nonNull(dto.getCompletedTime())) {
            builder.completedTime(dto.getCompletedTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        return builder.status(dto.getStatus())
                .mediaCount(dto.getMediaCount())
                .name(dto.getJobName())
                .errorCode(dto.getCode())
                .jobId(dto.getJobId())
                .fileId(dto.getFileId())
                .dockSn(dto.getDockSn())
                .workspaceId(dto.getWorkspaceId())
                .taskType(Optional.ofNullable(dto.getTaskType()).map(WaylineTaskTypeEnum::getVal).orElse(null))
                .waylineType(Optional.ofNullable(dto.getWaylineType()).map(WaylineTemplateTypeEnum::getVal).orElse(null))
                .username(dto.getUsername())
                .rthAltitude(dto.getRthAltitude())
                .outOfControlAction(dto.getOutOfControlAction())
                .parentId(dto.getParentId())
                .build();
    }

    @Override
    public void updateJobStatus(String workspaceId, String jobId, UpdateJobParam param) {
        Optional<WaylineJobDTO> waylineJobOpt = this.getJobByJobId(workspaceId, jobId);
        if (waylineJobOpt.isEmpty()) {
            throw new RuntimeException("The job does not exist.");
        }
        WaylineJobDTO waylineJob = waylineJobOpt.get();
        WaylineJobStatusEnum statusEnum = this.getWaylineState(waylineJob.getDockSn());
        if (statusEnum.getEnd() || WaylineJobStatusEnum.PENDING == statusEnum) {
            throw new RuntimeException("The wayline job status does not match, and the operation cannot be performed.");
        }

        switch (param.getStatus()) {
            case PAUSE:
                pauseJob(workspaceId, waylineJob.getDockSn(), jobId, statusEnum);
                break;
            case RESUME:
                resumeJob(workspaceId, waylineJob.getDockSn(), jobId, statusEnum);
                break;
        }

    }

    public WaylineJobStatusEnum getWaylineState(String dockSn) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty() || !StringUtils.hasText(dockOpt.get().getChildDeviceSn())) {
            return WaylineJobStatusEnum.UNKNOWN;
        }
        Optional<OsdDockReceiver> dockOsdOpt = deviceRedisService.getDeviceOsd(dockSn, OsdDockReceiver.class);
        Optional<OsdSubDeviceReceiver> deviceOsdOpt = deviceRedisService.getDeviceOsd(dockOpt.get().getChildDeviceSn(), OsdSubDeviceReceiver.class);
        if (dockOsdOpt.isEmpty() || deviceOsdOpt.isEmpty() || DockModeCodeEnum.WORKING != dockOsdOpt.get().getModeCode()) {
            return WaylineJobStatusEnum.UNKNOWN;
        }

        OsdSubDeviceReceiver osdDevice = deviceOsdOpt.get();
        if (DeviceModeCodeEnum.WAYLINE == osdDevice.getModeCode()
                || DeviceModeCodeEnum.MANUAL == osdDevice.getModeCode()
                || DeviceModeCodeEnum.TAKEOFF_AUTO == osdDevice.getModeCode()) {
            if (StringUtils.hasText(waylineRedisService.getPausedWaylineJobId(dockSn))) {
                return WaylineJobStatusEnum.PAUSED;
            }
            if (waylineRedisService.getRunningWaylineJob(dockSn).isPresent()) {
                return WaylineJobStatusEnum.IN_PROGRESS;
            }
        }
        return WaylineJobStatusEnum.UNKNOWN;
    }

    private void pauseJob(String workspaceId, String dockSn, String jobId, WaylineJobStatusEnum statusEnum) {
        if (WaylineJobStatusEnum.PAUSED == statusEnum && jobId.equals(waylineRedisService.getPausedWaylineJobId(dockSn))) {
            waylineRedisService.setPausedWaylineJob(dockSn, jobId);
            return;
        }

        ServiceReply reply = messageSender.publishServicesTopic(
                dockSn, WaylineMethodEnum.FLIGHT_TASK_PAUSE.getMethod(), "", jobId);
        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            throw new RuntimeException("Failed to pause wayline job. Error Code: " + reply.getResult());
        }
        waylineRedisService.delRunningWaylineJob(dockSn);
        waylineRedisService.setPausedWaylineJob(dockSn, jobId);
    }

    private void resumeJob(String workspaceId, String dockSn, String jobId, WaylineJobStatusEnum statusEnum) {
        Optional<EventsReceiver<WaylineTaskProgressReceiver>> runningDataOpt = waylineRedisService.getRunningWaylineJob(dockSn);
        if (WaylineJobStatusEnum.IN_PROGRESS == statusEnum && jobId.equals(runningDataOpt.map(EventsReceiver::getSn).get())) {
            waylineRedisService.setRunningWaylineJob(dockSn, runningDataOpt.get());
            return;
        }
        ServiceReply reply = messageSender.publishServicesTopic(
                dockSn, WaylineMethodEnum.FLIGHT_TASK_RESUME.getMethod(), "", jobId);
        if (ResponseResult.CODE_SUCCESS != reply.getResult()) {
            throw new RuntimeException("Failed to resume wayline job. Error Code: " + reply.getResult());
        }

        runningDataOpt.ifPresent(runningData -> waylineRedisService.setRunningWaylineJob(dockSn, runningData));
        waylineRedisService.delPausedWaylineJob(dockSn);

        if (deviceService.checkDockDrcMode(dockSn)) {
            drcService.deviceDrcExit(workspaceId, DrcModeParam.builder().dockSn(dockSn)
                    .clientId(drcService.getDrcModeInRedis(dockSn)).build());
        }

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
                .status(WaylineJobStatusEnum.IN_PROGRESS.getVal() == entity.getStatus() &&
                        entity.getJobId().equals(waylineRedisService.getPausedWaylineJobId(entity.getDockSn())) ?
                                WaylineJobStatusEnum.PAUSED.getVal() : entity.getStatus())
                .code(entity.getErrorCode())
                .beginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getBeginTime()), ZoneId.systemDefault()))
                .endTime(Objects.nonNull(entity.getEndTime()) ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getEndTime()), ZoneId.systemDefault()) : null)
                .executeTime(Objects.nonNull(entity.getExecuteTime()) ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getExecuteTime()), ZoneId.systemDefault()) : null)
                .completedTime(WaylineJobStatusEnum.find(entity.getStatus()).getEnd() ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getUpdateTime()), ZoneId.systemDefault()) : null)
                .taskType(WaylineTaskTypeEnum.find(entity.getTaskType()))
                .waylineType(WaylineTemplateTypeEnum.find(entity.getWaylineType()))
                .rthAltitude(entity.getRthAltitude())
                .outOfControlAction(entity.getOutOfControlAction())
                .mediaCount(entity.getMediaCount());

        if (Objects.nonNull(entity.getEndTime())) {
            builder.endTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getEndTime()), ZoneId.systemDefault()));
        }
        if (WaylineJobStatusEnum.IN_PROGRESS.getVal() == entity.getStatus()) {
            builder.progress(waylineRedisService.getRunningWaylineJob(entity.getDockSn())
                    .map(EventsReceiver::getOutput)
                    .map(WaylineTaskProgressReceiver::getProgress)
                    .map(WaylineTaskProgress::getPercent)
                    .orElse(null));
        }

        if (entity.getMediaCount() == 0) {
            return builder.build();
        }

        // sync the number of media files
        String key = RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + entity.getDockSn();
        String countKey = RedisConst.MEDIA_FILE_PREFIX + entity.getDockSn();
        Object mediaFileCount = RedisOpsUtils.hashGet(countKey, entity.getJobId());
        if (Objects.nonNull(mediaFileCount)) {
            builder.uploadedCount(((MediaFileCountDTO) mediaFileCount).getUploadedCount())
                    .uploading(RedisOpsUtils.checkExist(key) && entity.getJobId().equals(((MediaFileCountDTO)RedisOpsUtils.get(key)).getJobId()));
            return builder.build();
        }

        int uploadedSize = fileService.getFilesByWorkspaceAndJobId(entity.getWorkspaceId(), entity.getJobId()).size();
        // All media for this job have been uploaded.
        if (uploadedSize >= entity.getMediaCount()) {
            return builder.uploadedCount(uploadedSize).build();
        }
        RedisOpsUtils.hashSet(countKey, entity.getJobId(),
                MediaFileCountDTO.builder()
                        .jobId(entity.getJobId())
                        .mediaCount(entity.getMediaCount())
                        .uploadedCount(uploadedSize).build());
        return builder.build();
    }
}
