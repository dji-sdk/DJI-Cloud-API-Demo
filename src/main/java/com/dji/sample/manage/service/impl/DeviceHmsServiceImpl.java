package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.component.mqtt.model.MapKeyConst;
import com.dji.sample.component.mqtt.model.TopicConst;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.impl.SendMessageServiceImpl;
import com.dji.sample.component.websocket.service.impl.WebSocketManageServiceImpl;
import com.dji.sample.manage.dao.IDeviceHmsMapper;
import com.dji.sample.manage.model.common.HmsJsonUtil;
import com.dji.sample.manage.model.common.HmsMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceHmsDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.entity.DeviceHmsEntity;
import com.dji.sample.manage.model.enums.HmsEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceHmsQueryParam;
import com.dji.sample.manage.model.receiver.DeviceHmsReceiver;
import com.dji.sample.manage.model.receiver.HmsArgsReceiver;
import com.dji.sample.manage.service.IDeviceHmsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
@Service
@Transactional
public class DeviceHmsServiceImpl implements IDeviceHmsService {

    @Autowired
    private IDeviceHmsMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisOpsUtils redisOps;

    @Autowired
    private SendMessageServiceImpl sendMessageService;

    @Autowired
    private WebSocketManageServiceImpl webSocketManageService;

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_HMS)
    public void handleHms(CommonTopicReceiver receiver, MessageHeaders headers) {
    }

    @Override
    public PaginationData<DeviceHmsDTO> getDeviceHmsByParam(DeviceHmsQueryParam param) {
        LambdaQueryWrapper<DeviceHmsEntity> queryWrapper = new LambdaQueryWrapper<DeviceHmsEntity>()
                .and(wrapper -> param.getDeviceSn().forEach(sn -> wrapper.eq(DeviceHmsEntity::getSn, sn).or()))
                .between(param.getBeginTime() != null && param.getEndTime() != null,
                        DeviceHmsEntity::getCreateTime, param.getBeginTime(), param.getEndTime())
                .eq(param.getUpdateTime() != null, DeviceHmsEntity::getUpdateTime, param.getUpdateTime())
                .eq(param.getLevel() != null, DeviceHmsEntity::getLevel, param.getLevel())
                .like(StringUtils.hasText(param.getMessage()) &&
                                HmsEnum.MessageLanguage.ZH.getLanguage().equals(param.getLanguage()),
                        DeviceHmsEntity::getMessageZh, param.getMessage())
                .like(StringUtils.hasText(param.getMessage()) &&
                                HmsEnum.MessageLanguage.EN.getLanguage().equals(param.getLanguage()),
                        DeviceHmsEntity::getMessageEn, param.getMessage())
                .orderByDesc(DeviceHmsEntity::getCreateTime);
        if (param.getPage() == null || param.getPageSize() == null) {
            param.setPage(1L);
            param.setPageSize(Long.valueOf(mapper.selectCount(queryWrapper)));
        }

        Page<DeviceHmsEntity> pagination = mapper.selectPage(new Page<>(param.getPage(), param.getPageSize()), queryWrapper);

        List<DeviceHmsDTO> deviceHmsList = pagination.getRecords().stream().map(this::entity2Dto).collect(Collectors.toList());

        return new PaginationData<DeviceHmsDTO>(deviceHmsList, new Pagination(pagination));
    }

    @Override
    public void updateUnreadHms(String deviceSn) {
        mapper.update(DeviceHmsEntity.builder().updateTime(System.currentTimeMillis()).build(),
                new LambdaUpdateWrapper<DeviceHmsEntity>()
                        .eq(DeviceHmsEntity::getSn, deviceSn)
                        .eq(DeviceHmsEntity::getUpdateTime, 0L));
        redisOps.del(RedisConst.HMS_PREFIX + deviceSn);
    }

    private DeviceHmsDTO entity2Dto(DeviceHmsEntity entity) {
        if (entity == null) {
            return null;
        }
        return DeviceHmsDTO.builder()
                .bid(entity.getBid())
                .tid(entity.getTid())
                .createTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getCreateTime()), ZoneId.systemDefault()))
                .updateTime(entity.getUpdateTime().intValue() == 0 ?
                        null : LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getUpdateTime()), ZoneId.systemDefault()))
                .sn(entity.getSn())
                .hmsId(entity.getHmsId())
                .key(entity.getHmsKey())
                .level(entity.getLevel())
                .module(entity.getModule())
                .messageEn(entity.getMessageEn())
                .messageZh(entity.getMessageZh())
                .build();
    }

}
