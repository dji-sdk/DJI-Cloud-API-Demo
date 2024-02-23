package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.manage.dao.IDeviceHmsMapper;
import com.dji.sample.manage.model.common.HmsJsonUtil;
import com.dji.sample.manage.model.common.HmsMessage;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceHmsDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.model.entity.DeviceHmsEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceHmsQueryParam;
import com.dji.sample.manage.service.IDeviceHmsService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.cloudapi.hms.*;
import com.dji.sdk.cloudapi.hms.api.AbstractHmsService;
import com.dji.sdk.common.Pagination;
import com.dji.sdk.common.PaginationData;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class DeviceHmsServiceImpl extends AbstractHmsService implements IDeviceHmsService {

    @Autowired
    private IDeviceHmsMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWebSocketMessageService sendMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    private static final Pattern PATTERN_KEY = Pattern.compile(
                    "(" +
                    Arrays.stream(HmsFormatKeyEnum.values())
                            .map(HmsFormatKeyEnum::getKey)
                            .collect(Collectors.joining("|")) +
                    ")");

    @Override
    public void hms(TopicEventsRequest<Hms> response, MessageHeaders headers) {
        String sn = response.getFrom();
        DeviceHmsEntity entity = DeviceHmsEntity.builder()
                .bid(response.getBid())
                .tid(response.getTid())
                .createTime(response.getTimestamp())
                .updateTime(0L)
                .sn(sn)
                .build();
        // Query all unread hms messages of the device in redis.
        Set<String> hmsMap = deviceRedisService.getAllHmsKeys(sn);

        List<DeviceHmsDTO> unReadList = new ArrayList<>();
        response.getData().getList()
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
        deviceRedisService.addEndHmsKeys(sn, unReadList.stream().map(DeviceHmsDTO::getKey).toArray(String[]::new));
        // push to the web
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(sn);
        if (deviceOpt.isEmpty()) {
            return;
        }
        sendMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.DEVICE_HMS.getCode(), TelemetryDTO.<List<DeviceHmsDTO>>builder().sn(sn).host(unReadList).build());
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
                                HmsMessageLanguageEnum.ZH.getLanguage().equals(param.getLanguage()),
                        DeviceHmsEntity::getMessageZh, param.getMessage())
                .like(StringUtils.hasText(param.getMessage()) &&
                                HmsMessageLanguageEnum.EN.getLanguage().equals(param.getLanguage()),
                        DeviceHmsEntity::getMessageEn, param.getMessage())
                .orderByDesc(DeviceHmsEntity::getCreateTime);
        if (param.getPage() == null || param.getPageSize() == null) {
            param.setPage(1L);
            param.setPageSize(Long.valueOf(mapper.selectCount(queryWrapper)));
        }

        Page<DeviceHmsEntity> pagination = mapper.selectPage(new Page<>(param.getPage(), param.getPageSize()), queryWrapper);

        List<DeviceHmsDTO> deviceHmsList = pagination.getRecords().stream().map(this::entity2Dto).collect(Collectors.toList());

        return new PaginationData<DeviceHmsDTO>(deviceHmsList, new Pagination(pagination.getCurrent(), pagination.getSize(), pagination.getTotal()));
    }

    @Override
    public void updateUnreadHms(String deviceSn) {
        mapper.update(DeviceHmsEntity.builder().updateTime(System.currentTimeMillis()).build(),
                new LambdaUpdateWrapper<DeviceHmsEntity>()
                        .eq(DeviceHmsEntity::getSn, deviceSn)
                        .eq(DeviceHmsEntity::getUpdateTime, 0L));
        // Delete unread messages cached in redis.
        deviceRedisService.delHmsKeysBySn(deviceSn);
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
    private void fillEntity(DeviceHmsEntity dto, DeviceHms receiver) {
        dto.setLevel(receiver.getLevel().getLevel());
        dto.setModule(receiver.getModule().getModule());
        dto.setHmsId(UUID.randomUUID().toString());
        DeviceDomainEnum domain = receiver.getDeviceType().getDomain();
        if (DeviceDomainEnum.DOCK == domain) {
            dto.setHmsKey(HmsFaqIdEnum.DOCK_TIP.getText() + receiver.getCode());
            return;
        }
        StringBuilder key = new StringBuilder(HmsFaqIdEnum.FPV_TIP.getText()).append(receiver.getCode());

        if (receiver.getInTheSky()) {
            key.append(HmsInTheSkyEnum.IN_THE_SKY.getText());
        }
        dto.setHmsKey(key.toString());
    }

    /**
     * Replace wildcards in messages according to the relevant rules.
     * Please refer to the documentation for splicing rules.
     * @param dto
     * @param args
     */
    private void fillMessage(DeviceHmsEntity dto, DeviceHmsArgs args) {
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
    private Map<String, String> fillKeyArgs(String l, DeviceHmsArgs hmsArgs) {
        Map<String, String> args = new HashMap<>();
        args.put(HmsFormatKeyEnum.ALARM_ID.getKey(), Objects.nonNull(hmsArgs.getAlarmId()) ? Long.toHexString(hmsArgs.getAlarmId()) : null);
        args.put(HmsFormatKeyEnum.COMPONENT_INDEX.getKey(),
                Objects.nonNull(hmsArgs.getComponentIndex()) ? String.valueOf(hmsArgs.getComponentIndex() + 1) : null);
        if (Objects.nonNull(hmsArgs.getSensorIndex())) {
            args.put(HmsFormatKeyEnum.INDEX.getKey(), String.valueOf(hmsArgs.getSensorIndex() + 1));

            HmsBatteryIndexEnum hmsBatteryIndexEnum = Optional.ofNullable(hmsArgs.getSensorIndex())
                    .filter(arg -> arg <= 1).map(HmsBatteryIndexEnum::find).orElse(null);
            HmsDockCoverIndexEnum hmsDockCoverIndexEnum = Optional.ofNullable(hmsArgs.getSensorIndex())
                    .filter(arg -> arg <= 1).map(HmsDockCoverIndexEnum::find).orElse(null);
            HmsChargingRodIndexEnum hmsChargingRodIndexEnum = Optional.ofNullable(hmsArgs.getSensorIndex())
                    .filter(arg -> arg <= 3).map(HmsChargingRodIndexEnum::find).orElse(null);

            switch (l) {
                case "zh":
                    args.put(HmsFormatKeyEnum.BATTERY_INDEX.getKey(), Optional.ofNullable(hmsBatteryIndexEnum)
                            .map(HmsBatteryIndexEnum::getZh).orElse(null));
                    args.put(HmsFormatKeyEnum.DOCK_COVER_INDEX.getKey(), Optional.ofNullable(hmsDockCoverIndexEnum)
                            .map(HmsDockCoverIndexEnum::getZh).orElse(null));
                    args.put(HmsFormatKeyEnum.CHARGING_ROD_INDEX.getKey(), Optional.ofNullable(hmsChargingRodIndexEnum)
                            .map(HmsChargingRodIndexEnum::getZh).orElse(null));
                    break;
                case "en":
                    args.put(HmsFormatKeyEnum.BATTERY_INDEX.getKey(), Optional.ofNullable(hmsBatteryIndexEnum)
                            .map(HmsBatteryIndexEnum::getEn).orElse(null));
                    args.put(HmsFormatKeyEnum.DOCK_COVER_INDEX.getKey(), Optional.ofNullable(hmsDockCoverIndexEnum)
                            .map(HmsDockCoverIndexEnum::getEn).orElse(null));
                    args.put(HmsFormatKeyEnum.CHARGING_ROD_INDEX.getKey(), Optional.ofNullable(hmsChargingRodIndexEnum)
                            .map(HmsChargingRodIndexEnum::getEn).orElse(null));
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
    private String format(String l, String format, DeviceHmsArgs hmsArgs) {
        Map<String, String> args = fillKeyArgs(l, hmsArgs);
        List<String> list = parse(format);
        StringBuilder sb = new StringBuilder();
        for (String word : list) {
            if (!StringUtils.hasText(word)) {
                continue;
            }
            sb.append(args.getOrDefault(word, word));
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
