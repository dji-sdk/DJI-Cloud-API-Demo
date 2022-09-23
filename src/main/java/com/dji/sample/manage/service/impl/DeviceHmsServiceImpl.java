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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Pattern PATTERN_KEY = Pattern.compile(
            HmsEnum.FormatKeyEnum.KEY_START +
                    "(" +
                    Arrays.stream(HmsEnum.FormatKeyEnum.values())
                            .map(HmsEnum.FormatKeyEnum::getKey)
                            .collect(Collectors.joining("|")) +
                    ")");

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_HMS)
    public void handleHms(CommonTopicReceiver receiver, MessageHeaders headers) {
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        String sn  = topic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                topic.indexOf(TopicConst.EVENTS_SUF));

        DeviceHmsEntity entity = DeviceHmsEntity.builder()
                .bid(receiver.getBid())
                .tid(receiver.getTid())
                .createTime(receiver.getTimestamp())
                .updateTime(0L)
                .sn(sn)
                .build();
        String key = RedisConst.HMS_PREFIX + sn;
        // Query all unread hms messages of the device in redis.
        Set<String> hmsMap = redisOps.listGetAll(key).stream().map(String::valueOf).collect(Collectors.toSet());

        DeviceDTO device = (DeviceDTO) redisOps.get(RedisConst.DEVICE_ONLINE_PREFIX + sn);

        List<DeviceHmsDTO> unReadList = new ArrayList<>();
        objectMapper.convertValue(((Map) (receiver.getData())).get(MapKeyConst.LIST),
                new TypeReference<List<DeviceHmsReceiver>>() {})
                .forEach(hmsReceiver -> {
                    final DeviceHmsEntity hms = entity.clone();
                    this.fillEntity(hms, hmsReceiver);
                    // The same unread hms are no longer incremented.
                    if (hmsMap.contains(hms.getHmsKey())) {
                        return;
                    }
                    this.fillMessage(hms, hmsReceiver.getArgs());
                    unReadList.add(entity2Dto(hms));
                    mapper.insert(hms);
                });

        if (unReadList.isEmpty()) {
            return;
        }
        redisOps.listRPush(key, unReadList.stream().map(DeviceHmsDTO::getKey).toArray(String[]::new));
        // push to the web
        Collection<ConcurrentWebSocketSession> sessions = webSocketManageService.getValueWithWorkspaceAndUserType(
                device.getWorkspaceId(), UserTypeEnum.WEB.getVal());
        sendMessageService.sendBatch(sessions, CustomWebSocketMessage.builder()
                .bizCode(BizCodeEnum.DEVICE_HMS.getCode())
                .data(TelemetryDTO.<List<DeviceHmsDTO>>builder().sn(sn).host(unReadList).build())
                .timestamp(System.currentTimeMillis())
                .build());
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
        // Delete unread messages cached in redis.
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

    /**
     * Populate the received data into the entity. Please refer to the documentation for splicing rules.
     * @param dto
     * @param receiver
     */
    private void fillEntity(DeviceHmsEntity dto, DeviceHmsReceiver receiver) {
        dto.setLevel(receiver.getLevel());
        dto.setModule(receiver.getModule());
        dto.setHmsId(UUID.randomUUID().toString());

        if (HmsEnum.DomainType.DRONE_NEST.getDomain().equals(receiver.getDomainType())) {
            dto.setHmsKey(HmsEnum.HmsFaqIdEnum.DOCK_TIP.getText() + receiver.getCode());
            return;
        }
        StringBuilder key = new StringBuilder(HmsEnum.HmsFaqIdEnum.FPV_TIP.getText()).append(receiver.getCode());

        if (receiver.getInTheSky() == HmsEnum.IN_THE_SKY.getVal()) {
            key.append(HmsEnum.IN_THE_SKY.getText());
        }
        dto.setHmsKey(key.toString());
    }

    /**
     * Replace wildcards in messages according to the relevant rules.
     * Please refer to the documentation for splicing rules.
     * @param dto
     * @param args
     */
    private void fillMessage(DeviceHmsEntity dto, HmsArgsReceiver args) {
        HmsMessage hmsMessage = HmsJsonUtil.get(dto.getHmsKey());
        String zh = StringUtils.hasText(hmsMessage.getZh()) ? hmsMessage.getZh() : String.format("未知错误（%s）", dto.getHmsKey());
        String en = StringUtils.hasText(hmsMessage.getEn()) ? hmsMessage.getEn() : String.format("Unknown(%s)", dto.getHmsKey());//

        dto.setMessageZh(format(Locale.CHINESE.getLanguage(), zh, args));
        dto.setMessageEn(format(Locale.ENGLISH.getLanguage(), en, args));
    }

    /**
     * Set the matching parameters for key.
     * @param l     language: zh or en
     * @param hmsArgs
     * @return
     */
    private List<String> fillKeyArgs(String l, HmsArgsReceiver hmsArgs) {
        List<String> args = new ArrayList<>();
        args.add(Objects.nonNull(hmsArgs.getAlarmId()) ? Long.toHexString(hmsArgs.getAlarmId()) : null);
        args.add(Objects.nonNull(hmsArgs.getComponentIndex()) ? String.valueOf(hmsArgs.getComponentIndex() + 1) : null);
        if (Objects.nonNull(hmsArgs.getSensorIndex())) {
            args.add(String.valueOf(hmsArgs.getSensorIndex() + 1));

            HmsEnum.HmsBatteryIndexEnum hmsBatteryIndexEnum = HmsEnum.HmsBatteryIndexEnum.find(hmsArgs.getSensorIndex());
            HmsEnum.HmsDockCoverIndexEnum hmsDockCoverIndexEnum = HmsEnum.HmsDockCoverIndexEnum.find(hmsArgs.getSensorIndex());
            HmsEnum.HmsChargingRodIndexEnum hmsChargingRodIndexEnum = HmsEnum.HmsChargingRodIndexEnum.find(hmsArgs.getSensorIndex());

            switch (l) {
                case "zh":
                    args.add(hmsBatteryIndexEnum.getZh());
                    args.add(hmsDockCoverIndexEnum.getZh());
                    args.add(hmsChargingRodIndexEnum.getZh());
                    break;
                case "en":
                    args.add(hmsBatteryIndexEnum.getEn());
                    args.add(hmsDockCoverIndexEnum.getEn());
                    args.add(hmsChargingRodIndexEnum.getEn());
                    break;
                default:
                    break;
            }

        }
        return args;
    }

    /**
     * Returns a formatted string using the specified locale, format string, and arguments.
     * @param l language: zh or en
     * @param format
     * @param hmsArgs
     * @return
     */
    private String format(String l, String format, HmsArgsReceiver hmsArgs) {
        List<String> args = fillKeyArgs(l, hmsArgs);
        List<String> list = parse(format);
        StringBuilder sb = new StringBuilder();
        for (String word : list) {
            if (!StringUtils.hasText(word)) {
                continue;
            }
            HmsEnum.FormatKeyEnum keyEnum = HmsEnum.FormatKeyEnum.find(word.substring(1));
            sb.append(HmsEnum.FormatKeyEnum.KEY_START != word.charAt(0) || HmsEnum.FormatKeyEnum.UNKNOWN == keyEnum ?
                    word : args.get(keyEnum.getIndex()));
        }
        return sb.toString();
    }

    /**
     * Finds format specifiers in the format string.
     * @param s
     * @return
     */
    private List<String> parse(String s) {
        List<String> list = new ArrayList<>();
        Matcher matcher = PATTERN_KEY.matcher(s);
        for (int i = 0; i < s.length(); ) {
            if (matcher.find(i)) {
                if (matcher.start() != i) {
                    list.add(s.substring(i, matcher.start()));
                }
                list.add(matcher.group());
                i = matcher.end();
            } else {
                list.add(s.substring(i));
                break;
            }
        }
        return list;
    }
}
