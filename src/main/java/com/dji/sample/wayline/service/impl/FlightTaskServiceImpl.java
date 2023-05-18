package com.dji.sample.wayline.service.impl;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.media.model.MediaFileCountDTO;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.dto.WaylineJobKey;
import com.dji.sample.wayline.model.dto.WaylineTaskProgressReceiver;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.model.enums.WaylineTaskTypeEnum;
import com.dji.sample.wayline.service.IFlightTaskService;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.dji.sample.wayline.service.IWaylineRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Service
@Slf4j
public class FlightTaskServiceImpl implements IFlightTaskService {

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ISendMessageService websocketMessageService;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IWaylineRedisService waylineRedisService;

    /**
     * Handle the progress messages of the flight tasks reported by the dock.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        EventsReceiver<WaylineTaskProgressReceiver> eventsReceiver = mapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<WaylineTaskProgressReceiver>>(){});
        eventsReceiver.setBid(receiver.getBid());
        eventsReceiver.setSn(receiver.getGateway());

        WaylineTaskProgressReceiver output = eventsReceiver.getOutput();

        log.info("Task progress: {}", output.getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("Task progress ===> Error code: " + eventsReceiver.getResult());
        }

        EventsResultStatusEnum statusEnum = EventsResultStatusEnum.find(output.getStatus());
        waylineRedisService.setRunningWaylineJob(receiver.getGateway(), eventsReceiver);

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(receiver.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }

        if (statusEnum.getEnd()) {
            handleEndStatus(receiver, statusEnum, output.getExt().getMediaCount(), eventsReceiver.getResult(), deviceOpt.get());
        }

        websocketMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                        BizCodeEnum.FLIGHT_TASK_PROGRESS.getCode(), eventsReceiver);

        return receiver;
    }

    private void handleEndStatus(CommonTopicReceiver receiver, EventsResultStatusEnum statusEnum, int mediaCount, int code, DeviceDTO dock) {

        WaylineJobDTO job = WaylineJobDTO.builder()
                .jobId(receiver.getBid())
                .status(WaylineJobStatusEnum.SUCCESS.getVal())
                .completedTime(LocalDateTime.now())
                .mediaCount(mediaCount)
                .build();

        // record the update of the media count.
        if (Objects.nonNull(job.getMediaCount()) && job.getMediaCount() != 0) {
            RedisOpsUtils.hashSet(RedisConst.MEDIA_FILE_PREFIX + receiver.getGateway(), job.getJobId(),
                    MediaFileCountDTO.builder().jobId(receiver.getBid()).mediaCount(job.getMediaCount()).uploadedCount(0).build());
        }

        if (EventsResultStatusEnum.OK != statusEnum) {
            job.setCode(code);
            job.setStatus(WaylineJobStatusEnum.FAILED.getVal());
        }

        waylineRedisService.getConditionalWaylineJob(receiver.getBid()).ifPresent(waylineJob ->
                retryPrepareConditionJob(new WaylineJobKey(dock.getWorkspaceId(), dock.getDeviceSn(), receiver.getBid()), waylineJob));
        waylineJobService.updateJob(job);
        waylineRedisService.delRunningWaylineJob(receiver.getGateway());
        waylineRedisService.delPausedWaylineJob(receiver.getBid());
        waylineRedisService.delBlockedWaylineJobId(receiver.getGateway());

    }

    /**
     * Notifications will be received through this interface when tasks are ready on the device.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_READY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleTaskNotifications(CommonTopicReceiver receiver, MessageHeaders headers) {
        String dockSn  = receiver.getGateway();
        Set<String> flightIds = mapper.convertValue(receiver.getData(),
                new TypeReference<Map<String, Set<String>>>(){}).get(MapKeyConst.FLIGHT_IDS);

        log.info("ready task list：{}", Arrays.toString(flightIds.toArray()));
        // Check conditional task blocking status.
        String blockedId = waylineRedisService.getBlockedWaylineJobId(dockSn);
        if (StringUtils.hasText(blockedId)) {
            log.info("The dock is in a state of wayline congestion, and the task will not be executed.");
            return null;
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            log.info("The dock is offline.");
            return null;
        }
        DeviceDTO device = deviceOpt.get();
        Optional<WaylineJobDTO> jobOpt = waylineJobService.getJobsByConditions(device.getWorkspaceId(), flightIds, WaylineJobStatusEnum.PENDING)
                .stream().filter(job -> flightIds.contains(job.getJobId()))
                .sorted(Comparator.comparingInt(a -> a.getTaskType().getVal()))
                .min(Comparator.comparing(WaylineJobDTO::getBeginTime));
        if (jobOpt.isEmpty()) {
            return receiver;
        }
        executeReadyTask(jobOpt.get());

        return receiver;
    }

    private void executeReadyTask(WaylineJobDTO waylineJob) {
        try {
            boolean isExecute = waylineJobService.executeFlightTask(waylineJob.getWorkspaceId(), waylineJob.getJobId());
            if (isExecute || WaylineTaskTypeEnum.CONDITION != waylineJob.getTaskType()) {
                return;
            }
            Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(waylineJob.getJobId());
            if (waylineJobOpt.isEmpty()) {
                log.info("The conditional job has expired and will no longer be executed.");
                return;
            }
            waylineJob = waylineJobOpt.get();
            this.retryPrepareConditionJob(new WaylineJobKey(waylineJob.getWorkspaceId(), waylineJob.getDockSn(), waylineJob.getJobId()), waylineJob);
        } catch (Exception e) {
            log.error("Failed to execute task. ID: {}, Name:{}", waylineJob.getJobId(), waylineJob.getJobName());
            this.retryPrepareConditionJob(new WaylineJobKey(waylineJob.getWorkspaceId(), waylineJob.getDockSn(), waylineJob.getJobId()), waylineJob);
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 10, fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    private void prepareWaylineJob() {
        Optional<WaylineJobKey> jobKeyOpt = waylineRedisService.getNearestPreparedWaylineJob();
        if (jobKeyOpt.isEmpty()) {
            return;
        }
        // format: {workspace_id}:{dock_sn}:{job_id}
        WaylineJobKey jobKey = jobKeyOpt.get();
        log.info("Check the prepared tasks of the wayline. {}", jobKey.toString());

        WaylineJobDTO job = WaylineJobDTO.builder()
                .jobId(jobKey.getJobId())
                .status(WaylineJobStatusEnum.FAILED.getVal())
                .executeTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .code(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        Optional<WaylineJobDTO> waylineJobOpt = getPreparedJob(jobKey, job);
        if (waylineJobOpt.isEmpty()) {
            return;
        }

        WaylineJobDTO waylineJob = waylineJobOpt.get();
        try {
            ResponseResult result = waylineJobService.publishOneFlightTask(waylineJob);
            if (ResponseResult.CODE_SUCCESS == result.getCode()) {
                return;
            }
            log.info("Failed to prepare the task. {}", result.getMessage());
            job.setCode(result.getCode());
            waylineJobService.updateJob(job);
            // Retry if the end time has not been exceeded.
            this.retryPrepareConditionJob(jobKey, waylineJob);
        } catch (Exception e) {
            log.info("Failed to prepare the task. {}", e.getLocalizedMessage());
            waylineJobService.updateJob(job);
            this.retryPrepareConditionJob(jobKey, waylineJob);
        }
    }

    private boolean checkTime(long time) {
        // prepare the task one day in advance.
        int offset = 86_400_000;
        return System.currentTimeMillis() + offset >= time;
    }

    private Optional<WaylineJobDTO> getPreparedJob(WaylineJobKey jobKey, WaylineJobDTO job) {
        long time = waylineRedisService.getPreparedWaylineJobTime(jobKey).longValue();
        if (!checkTime(time)) {
            return Optional.empty();
        }

        Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(jobKey.getJobId());
        // Determine whether the conditional task or the scheduled task has expired.
        if (waylineJobOpt.isEmpty()) {
            waylineJobOpt = waylineJobService.getJobByJobId(jobKey.getWorkspaceId(), jobKey.getJobId());
            if (waylineJobOpt.isEmpty() || waylineJobOpt.get().getEndTime().isBefore(LocalDateTime.now())) {
                job.setCode(CommonErrorEnum.REDIS_DATA_NOT_FOUND.getErrorCode());
                waylineJobService.updateJob(job);
                return Optional.empty();
            }
        }
        waylineRedisService.removePreparedWaylineJob(jobKey);
        return waylineJobOpt;
    }

    private void retryPrepareConditionJob(WaylineJobKey jobKey, WaylineJobDTO waylineJob) {
        if (WaylineTaskTypeEnum.CONDITION != waylineJob.getTaskType()) {
            return;
        }
        // If the end time is exceeded, no more retries will be made.
        waylineRedisService.delConditionalWaylineJob(jobKey.getJobId());
        if (waylineJob.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < System.currentTimeMillis()) {
            return;
        }

        Optional<WaylineJobDTO> childJobOpt = waylineJobService.createWaylineJobByParent(jobKey.getWorkspaceId(), jobKey.getJobId());
        if (childJobOpt.isEmpty()) {
            log.error("Failed to create wayline job.");
            return;
        }

        WaylineJobDTO newJob = childJobOpt.get();
        newJob.setBeginTime(LocalDateTime.now().plusSeconds(RedisConst.WAYLINE_JOB_BLOCK_TIME));
        boolean isAdd = waylineRedisService.addPreparedWaylineJob(newJob);
        if (!isAdd) {
            log.error("Failed to create wayline job. {}", newJob.getJobId());
            return;
        }

        waylineJob.setJobId(newJob.getJobId());
        waylineRedisService.setConditionalWaylineJob(waylineJob);
    }
}
