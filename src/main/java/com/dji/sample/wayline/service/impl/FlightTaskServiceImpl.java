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
import com.dji.sample.wayline.model.dto.ConditionalWaylineJobKey;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.dto.WaylineTaskProgressReceiver;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.service.IFlightTaskService;
import com.dji.sample.wayline.service.IWaylineJobService;
import com.dji.sample.wayline.service.IWaylineRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
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
        String receivedTopic = String.valueOf(headers.get(MqttHeaders.RECEIVED_TOPIC));
        String dockSn  = receivedTopic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                receivedTopic.indexOf(TopicConst.EVENTS_SUF));
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
        waylineRedisService.setRunningWaylineJob(dockSn, eventsReceiver);

        if (statusEnum.getEnd()) {
            WaylineJobDTO job = WaylineJobDTO.builder()
                    .jobId(receiver.getBid())
                    .status(WaylineJobStatusEnum.SUCCESS.getVal())
                    .completedTime(LocalDateTime.now())
                    .mediaCount(output.getExt().getMediaCount())
                    .build();

            // record the update of the media count.
            if (Objects.nonNull(job.getMediaCount()) && job.getMediaCount() != 0) {
                RedisOpsUtils.hashSet(RedisConst.MEDIA_FILE_PREFIX + receiver.getGateway(), job.getJobId(),
                        MediaFileCountDTO.builder().jobId(receiver.getBid()).mediaCount(job.getMediaCount()).uploadedCount(0).build());
            }

            if (EventsResultStatusEnum.OK != statusEnum) {
                job.setCode(eventsReceiver.getResult());
                job.setStatus(WaylineJobStatusEnum.FAILED.getVal());
            }

            waylineJobService.updateJob(job);
            waylineRedisService.delRunningWaylineJob(dockSn);
            waylineRedisService.delPausedWaylineJob(receiver.getBid());
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(receiver.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }
        websocketMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                        BizCodeEnum.FLIGHT_TASK_PROGRESS.getCode(), eventsReceiver);

        return receiver;
    }

    /**
     * Notifications will be received through this interface when tasks are ready on the device.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_READY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public CommonTopicReceiver handleTaskNotifications(CommonTopicReceiver receiver, MessageHeaders headers) {
        String receivedTopic = String.valueOf(headers.get(MqttHeaders.RECEIVED_TOPIC));
        String dockSn  = receivedTopic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                receivedTopic.indexOf(TopicConst.EVENTS_SUF));
        List<String> flightIds = mapper.convertValue(receiver.getData(),
                new TypeReference<Map<String, List<String>>>(){}).get(MapKeyConst.FLIGHT_IDS);

        log.info("ready task list：{}", Arrays.toString(flightIds.toArray()) );
        // Check conditional task blocking status.
        String blockedId = waylineRedisService.getBlockedWaylineJobId(dockSn);
        if (!StringUtils.hasText(blockedId)) {
            return null;
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            return null;
        }
        DeviceDTO device = deviceOpt.get();

        try {
            for (String jobId : flightIds) {
                boolean isExecute = waylineJobService.executeFlightTask(device.getWorkspaceId(), jobId);
                if (!isExecute) {
                    return null;
                }
                Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(jobId);
                if (waylineJobOpt.isEmpty()) {
                    log.info("The conditional job has expired and will no longer be executed.");
                    return receiver;
                }
                WaylineJobDTO waylineJob = waylineJobOpt.get();
                this.retryPrepareJob(new ConditionalWaylineJobKey(device.getWorkspaceId(), dockSn, jobId), waylineJob);
                return receiver;
            }
        } catch (Exception e) {
            log.error("Failed to execute conditional task.");
            e.printStackTrace();
        }
        return receiver;
    }

    @Scheduled(initialDelay = 10, fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    private void checkScheduledJob() {
        Object jobIdValue = RedisOpsUtils.zGetMin(RedisConst.WAYLINE_JOB_TIMED_EXECUTE);
        if (Objects.isNull(jobIdValue)) {
            return;
        }
        log.info("Check the timed tasks of the wayline. {}", jobIdValue);
        // format: {workspace_id}:{dock_sn}:{job_id}
        String[] jobArr = String.valueOf(jobIdValue).split(RedisConst.DELIMITER);
        double time = RedisOpsUtils.zScore(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
        long now = System.currentTimeMillis();
        int offset = 30_000;

        // Expired tasks are deleted directly.
        if (time < now - offset) {
            RedisOpsUtils.zRemove(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
            waylineJobService.updateJob(WaylineJobDTO.builder()
                    .jobId(jobArr[2])
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .executeTime(LocalDateTime.now())
                    .completedTime(LocalDateTime.now())
                    .code(HttpStatus.SC_REQUEST_TIMEOUT).build());
            return;
        }

        if (now <= time && time <= now + offset) {
            try {
                waylineJobService.executeFlightTask(jobArr[0], jobArr[2]);
            } catch (Exception e) {
                log.info("The scheduled task delivery failed.");
                waylineJobService.updateJob(WaylineJobDTO.builder()
                        .jobId(jobArr[2])
                        .status(WaylineJobStatusEnum.FAILED.getVal())
                        .executeTime(LocalDateTime.now())
                        .completedTime(LocalDateTime.now())
                        .code(HttpStatus.SC_INTERNAL_SERVER_ERROR).build());
            } finally {
                RedisOpsUtils.zRemove(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
            }
        }
    }

    @Scheduled(initialDelay = 10, fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    private void prepareConditionJob() {
        Optional<ConditionalWaylineJobKey> jobKeyOpt = waylineRedisService.getNearestConditionalWaylineJob();
        if (jobKeyOpt.isEmpty()) {
            return;
        }
        ConditionalWaylineJobKey jobKey = jobKeyOpt.get();
        log.info("Check the conditional tasks of the wayline. {}", jobKey.toString());
        // format: {workspace_id}:{dock_sn}:{job_id}
        double time = waylineRedisService.getConditionalWaylineJobTime(jobKey);
        long now = System.currentTimeMillis();
        // prepare the task one day in advance.
        int offset = 86_400_000;

        if (now + offset < time) {
            return;
        }

        WaylineJobDTO job = WaylineJobDTO.builder()
                .jobId(jobKey.getJobId())
                .status(WaylineJobStatusEnum.FAILED.getVal())
                .executeTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .code(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        try {
            Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(jobKey.getJobId());
            if (waylineJobOpt.isEmpty()) {
                job.setCode(CommonErrorEnum.REDIS_DATA_NOT_FOUND.getErrorCode());
                waylineJobService.updateJob(job);
                waylineRedisService.removePrepareConditionalWaylineJob(jobKey);
                return;
            }
            WaylineJobDTO waylineJob = waylineJobOpt.get();

            ResponseResult result = waylineJobService.publishOneFlightTask(waylineJob);
            waylineRedisService.removePrepareConditionalWaylineJob(jobKey);

            if (ResponseResult.CODE_SUCCESS == result.getCode()) {
                return;
            }

            // If the end time is exceeded, no more retries will be made.
            waylineRedisService.delConditionalWaylineJob(jobKey.getJobId());
            if (waylineJob.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - RedisConst.WAYLINE_JOB_BLOCK_TIME * 1000 < now) {
                return;
            }

            // Retry if the end time has not been exceeded.
            this.retryPrepareJob(jobKey, waylineJob);

        } catch (Exception e) {
            log.info("Failed to prepare the conditional task.");
            waylineJobService.updateJob(job);
        }

    }

    private void retryPrepareJob(ConditionalWaylineJobKey jobKey, WaylineJobDTO waylineJob) {
        Optional<WaylineJobDTO> childJobOpt = waylineJobService.createWaylineJobByParent(jobKey.getWorkspaceId(), jobKey.getJobId());
        if (childJobOpt.isEmpty()) {
            log.error("Failed to create wayline job.");
            return;
        }

        WaylineJobDTO newJob = childJobOpt.get();
        newJob.setBeginTime(LocalDateTime.now().plusSeconds(RedisConst.WAYLINE_JOB_BLOCK_TIME));
        boolean isAdd = waylineRedisService.addPrepareConditionalWaylineJob(newJob);
        if (!isAdd) {
            log.error("Failed to create wayline job. {}", newJob.getJobId());
            return;
        }

        waylineJob.setJobId(newJob.getJobId());
        waylineRedisService.setConditionalWaylineJob(waylineJob);
    }
}
