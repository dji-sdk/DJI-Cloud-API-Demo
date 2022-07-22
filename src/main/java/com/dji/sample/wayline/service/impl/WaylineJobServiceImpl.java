package com.dji.sample.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.model.ServiceReply;
import com.dji.sample.component.mqtt.model.ServicesMethodEnum;
import com.dji.sample.component.mqtt.model.TopicConst;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.wayline.dao.IWaylineJobMapper;
import com.dji.sample.wayline.model.dto.FlightTaskCreateDTO;
import com.dji.sample.wayline.model.dto.FlightTaskFileDTO;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.entity.WaylineJobEntity;
import com.dji.sample.wayline.model.param.CreateJobParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import com.dji.sample.wayline.service.IWaylineJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Override
    public Boolean createJob(CreateJobParam param, CustomClaim customClaim) throws SQLException {
        if (param == null) {
            return false;
        }
        WaylineJobEntity jobEntity = WaylineJobEntity.builder()
                .name(param.getName())
                .dockSn(param.getDockSn())
                .fileId(param.getFileId())
                .username(customClaim.getUsername())
                .workspaceId(customClaim.getWorkspaceId())
                .jobId(UUID.randomUUID().toString())
                .type(param.getType())
                .build();
        int id = mapper.insert(jobEntity);
        if (id <= 0) {
            return false;
        }
        if (param.isImmediate()) {
            publishFlightTask(jobEntity.getWorkspaceId(), jobEntity.getJobId());
        }
        return true;
    }

    @Override
    public void publishFlightTask(String workspaceId, String jobId) throws SQLException {
        // get job
        Optional<WaylineJobDTO> waylineJob = this.getJobByJobId(jobId);
        if (waylineJob.isEmpty()) {
            throw new IllegalArgumentException("Job doesn't exist.");
        }

        long expire = redisOps.getExpire(RedisConst.DEVICE_ONLINE_PREFIX + waylineJob.get().getDockSn());
        if (expire < 0) {
            throw new RuntimeException("Dock is offline.");
        }

        // get wayline file
        Optional<WaylineFileDTO> waylineFile = waylineFileService.getWaylineByWaylineId(workspaceId, waylineJob.get().getFileId());
        if (waylineFile.isEmpty()) {
            throw new IllegalArgumentException("Wayline file doesn't exist.");
        }

        // get file url
        URL url = waylineFileService.getObjectUrl(workspaceId, waylineFile.get().getWaylineId());

        WaylineJobDTO job = waylineJob.get();
        FlightTaskCreateDTO flightTask = FlightTaskCreateDTO.builder()
                .flightId(jobId)
                .type(job.getType())
                .file(FlightTaskFileDTO.builder()
                        .url(url.toString())
                        .sign(waylineFile.get().getSign())
                        .build())
                .build();

        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT +
                job.getDockSn() + TopicConst.SERVICES_SUF;
        CommonTopicResponse<Object> response = CommonTopicResponse.builder()
                .tid(UUID.randomUUID().toString())
                .bid(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .data(flightTask)
                .method(ServicesMethodEnum.FLIGHTTASK_CREATE.getMethod())
                .build();

        Optional<ServiceReply> serviceReplyOpt = messageSender.publishWithReply(topic, response);
        if (serviceReplyOpt.isEmpty()) {
            log.info("Timeout to receive reply.");
            throw new RuntimeException("Timeout to receive reply.");
        }
        if (serviceReplyOpt.get().getResult() != 0) {
            log.info("Error code: {}", serviceReplyOpt.get().getResult());
            throw new RuntimeException("Error code: " + serviceReplyOpt.get().getResult());
        }

        job.setBid(response.getBid());
        boolean isUpd = this.updateJob(job);
        if (!isUpd) {
            throw new SQLException("Failed to update data.");
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
                        .eq(WaylineJobEntity::getWorkspaceId, dto.getWorkspaceId())
                        .eq(WaylineJobEntity::getJobId, dto.getJobId()))
                > 0;
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

    private WaylineJobEntity dto2Entity(WaylineJobDTO dto) {
        WaylineJobEntity.WaylineJobEntityBuilder builder = WaylineJobEntity.builder();
        if (dto == null) {
            return builder.build();
        }
        return builder.type(dto.getType())
                .bid(dto.getBid())
                .name(dto.getJobName())
                .build();
    }

    private WaylineJobDTO entity2Dto(WaylineJobEntity entity) {
        if (entity == null) {
            return null;
        }
        return WaylineJobDTO.builder()
                .jobId(entity.getJobId())
                .bid(entity.getBid())
                .updateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getUpdateTime()), ZoneId.systemDefault()))
                .jobName(entity.getName())
                .fileId(entity.getFileId())
                .fileName(waylineFileService.getWaylineByWaylineId(entity.getWorkspaceId(), entity.getFileId())
                        .orElse(WaylineFileDTO.builder().build()).getName())
                .dockSn(entity.getDockSn())
                .dockName(deviceService.getDeviceBySn(entity.getDockSn())
                        .orElse(DeviceDTO.builder().build()).getNickname())
                .username(entity.getUsername())
                .workspaceId(entity.getWorkspaceId())
                .type(entity.getType())
                .build();
    }
}
