package com.dji.sample.wayline.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.media.model.MediaFileCountDTO;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.dto.WaylineTaskProgressReceiver;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.service.IFlightTaskService;
import com.dji.sample.wayline.service.IWaylineJobService;
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

import java.time.LocalDateTime;
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
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHT_TASK_PROGRESS, outputChannel = ChannelName.OUTBOUND)
    public void handleProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
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
        String key = RedisConst.WAYLINE_JOB_RUNNING_PREFIX + dockSn;
        RedisOpsUtils.setWithExpire(key, eventsReceiver, RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);

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
            RedisOpsUtils.del(key);
            RedisOpsUtils.del(RedisConst.WAYLINE_JOB_PAUSED_PREFIX + receiver.getBid());
        }

        DeviceDTO device = (DeviceDTO) RedisOpsUtils.get(RedisConst.DEVICE_ONLINE_PREFIX + receiver.getGateway());
        websocketMessageService.sendBatch(
                webSocketManageService.getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                CustomWebSocketMessage.builder()
                        .data(eventsReceiver)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.FLIGHT_TASK_PROGRESS.getCode())
                        .build());

        if (receiver.getNeedReply() == 1) {
            messageSender.publish(receivedTopic + TopicConst._REPLY_SUF,
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .method(EventsMethodEnum.FLIGHT_TASK_PROGRESS.getMethod())
                            .timestamp(System.currentTimeMillis())
                            .data(RequestsReply.success())
                            .build());
        }
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
}
