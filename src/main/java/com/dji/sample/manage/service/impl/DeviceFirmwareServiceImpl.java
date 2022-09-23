package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.impl.MessageSenderServiceImpl;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.component.websocket.service.impl.SendMessageServiceImpl;
import com.dji.sample.manage.dao.IDeviceFirmwareMapper;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareNoteDTO;
import com.dji.sample.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.dji.sample.manage.model.entity.DeviceFirmwareEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import com.dji.sample.manage.service.IDeviceFirmwareService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@Service
@Slf4j
public class DeviceFirmwareServiceImpl implements IDeviceFirmwareService {

    @Autowired
    private IDeviceFirmwareMapper mapper;

    @Autowired
    private RedisOpsUtils redisOps;

    @Autowired
    private MessageSenderServiceImpl messageSenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SendMessageServiceImpl webSocketMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Override
    public Optional<DeviceFirmwareDTO> getFirmware(String deviceName, String version) {
        return Optional.ofNullable(entity2Dto(mapper.selectOne(
                new LambdaQueryWrapper<DeviceFirmwareEntity>()
                        .eq(DeviceFirmwareEntity::getDeviceName, deviceName)
                        .eq(DeviceFirmwareEntity::getFirmwareVersion, version))));
    }

    @Override
    public Optional<DeviceFirmwareNoteDTO> getLatestFirmwareReleaseNote(String deviceName) {
        return Optional.ofNullable(entity2NoteDto(mapper.selectOne(
                new LambdaQueryWrapper<DeviceFirmwareEntity>()
                        .eq(DeviceFirmwareEntity::getDeviceName, deviceName)
                        .eq(DeviceFirmwareEntity::getStatus, true)
                        .orderByDesc(DeviceFirmwareEntity::getReleaseDate)
                        .last(" limit 1 "))));
    }

    @Override
    public List<DeviceOtaCreateParam> getDeviceOtaFirmware(List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<DeviceOtaCreateParam> deviceOtaList = new ArrayList<>();
        upgradeDTOS.forEach(upgradeDevice -> {
            boolean exist = redisOps.getExpire(RedisConst.DEVICE_ONLINE_PREFIX + upgradeDevice.getSn()) > 0;
            if (!exist) {
                throw new IllegalArgumentException("Device is offline.");
            }
            Optional<DeviceFirmwareDTO> firmwareOpt = this.getFirmware(
                    upgradeDevice.getDeviceName(), upgradeDevice.getProductVersion());
            if (firmwareOpt.isEmpty()) {
                throw new IllegalArgumentException("This firmware version does not exist.");
            }
            if (!firmwareOpt.get().getFirmwareStatus()) {
                throw new IllegalArgumentException("This firmware version is not available.");
            }
            DeviceOtaCreateParam ota = dto2OtaCreateDto(firmwareOpt.get());
            ota.setSn(upgradeDevice.getSn());
            ota.setFirmwareUpgradeType(upgradeDevice.getFirmwareUpgradeType());
            deviceOtaList.add(ota);
        });
        return deviceOtaList;
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_OTA_PROGRESS, outputChannel = ChannelName.OUTBOUND)
    public void handleOtaProgress(CommonTopicReceiver receiver, MessageHeaders headers) {
        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC).toString();
        String sn  = topic.substring((TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT).length(),
                topic.indexOf(TopicConst.EVENTS_SUF));

        EventsReceiver<EventsOutputReceiver> eventsReceiver = objectMapper.convertValue(receiver.getData(),
                new TypeReference<EventsReceiver<EventsOutputReceiver>>(){});
        eventsReceiver.setBid(receiver.getBid());
        eventsReceiver.setSn(sn);

        EventsOutputReceiver output = eventsReceiver.getOutput();
        log.info("SN: {}, {} ===> Upgrading progress: {}",
                sn, receiver.getMethod(), output.getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("SN: {}, {} ===> Error code: {}", sn, receiver.getMethod(), eventsReceiver.getResult());
        }

        DeviceDTO device = (DeviceDTO) redisOps.get(RedisConst.DEVICE_ONLINE_PREFIX + sn);
        String childDeviceSn = device.getChildDeviceSn();
        boolean upgrade = redisOps.getExpire(RedisConst.FIRMWARE_UPGRADING_PREFIX + sn) > 0;
        boolean childUpgrade = redisOps.getExpire(RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn) > 0;

        // Determine whether it is the ending state, delete the update state key in redis after the job ends.
        EventsResultStatusEnum statusEnum = EventsResultStatusEnum.find(output.getStatus());
        if (upgrade) {
            if (statusEnum.getEnd()) {
                // Delete the cache after the update is complete.
                redisOps.del(RedisConst.FIRMWARE_UPGRADING_PREFIX + sn);
            } else {
                // Update the update progress of the dock in redis.
                redisOps.setWithExpire(
                        RedisConst.FIRMWARE_UPGRADING_PREFIX + sn, output.getProgress().getPercent(),
                        RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
            }
        }
        if (childUpgrade) {
            if (statusEnum.getEnd()) {
                redisOps.del(RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn);
            } else {
                // Update the update progress of the drone in redis.
                redisOps.setWithExpire(
                        RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn, output.getProgress().getPercent(),
                        RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
            }
        }

        webSocketMessageService.sendBatch(
                webSocketManageService.getValueWithWorkspaceAndUserType(
                        device.getWorkspaceId(), UserTypeEnum.WEB.getVal()),
                CustomWebSocketMessage.builder()
                        .data(eventsReceiver)
                        .timestamp(System.currentTimeMillis())
                        .bizCode(receiver.getMethod())
                        .build());

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
    }

    private DeviceFirmwareNoteDTO entity2NoteDto (DeviceFirmwareEntity entity) {
        if (entity == null) {
            return null;
        }
        return DeviceFirmwareNoteDTO.builder()
                .deviceName(entity.getDeviceName())
                .productVersion(entity.getFirmwareVersion())
                .releasedTime(LocalDate.ofInstant(Instant.ofEpochMilli(entity.getReleaseDate()), ZoneId.systemDefault()))
                .releaseNote(entity.getReleaseNote())
                .build();
    }

    private DeviceFirmwareDTO entity2Dto (DeviceFirmwareEntity entity) {
        if (entity == null) {
            return null;
        }
        return DeviceFirmwareDTO.builder()
                .deviceName(entity.getDeviceName())
                .fileMd5(entity.getFileMd5())
                .fileSize(entity.getFileSize())
                .fileUrl(entity.getFileUrl())
                .firmwareId(entity.getFirmwareId())
                .fileName(entity.getFileName())
                .productVersion(entity.getFirmwareVersion())
                .releasedTime(LocalDate.ofInstant(Instant.ofEpochMilli(entity.getReleaseDate()), ZoneId.systemDefault()))
                .firmwareStatus(entity.getStatus())
                .build();
    }

    private DeviceOtaCreateParam dto2OtaCreateDto(DeviceFirmwareDTO dto) {
        if (dto == null) {
            return null;
        }
        return DeviceOtaCreateParam.builder()
                .fileSize(dto.getFileSize())
                .fileUrl(dto.getFileUrl())
                .fileName(dto.getFileName())
                .md5(dto.getFileMd5())
                .productVersion(dto.getProductVersion())
                .build();
    }
}
