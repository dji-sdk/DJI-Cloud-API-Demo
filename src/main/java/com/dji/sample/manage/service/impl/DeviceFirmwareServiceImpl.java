package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.impl.MessageSenderServiceImpl;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.component.websocket.service.impl.SendMessageServiceImpl;
import com.dji.sample.manage.dao.IDeviceFirmwareMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceFirmwareEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceFirmwareQueryParam;
import com.dji.sample.manage.model.param.DeviceFirmwareUploadParam;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import com.dji.sample.manage.service.IDeviceFirmwareService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.manage.service.IFirmwareModelService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    private MessageSenderServiceImpl messageSenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SendMessageServiceImpl webSocketMessageService;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private OssServiceContext ossServiceContext;

    @Autowired
    private IFirmwareModelService firmwareModelService;

    @Override
    public Optional<DeviceFirmwareDTO> getFirmware(String workspaceId, String deviceName, String version) {
        return Optional.ofNullable(entity2Dto(mapper.selectOne(
                new LambdaQueryWrapper<DeviceFirmwareEntity>()
                        .eq(DeviceFirmwareEntity::getWorkspaceId, workspaceId)
                        .eq(DeviceFirmwareEntity::getFirmwareVersion, version)
                        .eq(DeviceFirmwareEntity::getStatus, true),
                deviceName)));
    }

    @Override
    public Optional<DeviceFirmwareNoteDTO> getLatestFirmwareReleaseNote(String deviceName) {
        return Optional.ofNullable(entity2NoteDto(mapper.selectOne(
                Wrappers.lambdaQuery(DeviceFirmwareEntity.class)
                        .eq(DeviceFirmwareEntity::getStatus, true)
                        .orderByDesc(DeviceFirmwareEntity::getReleaseDate, DeviceFirmwareEntity::getFirmwareVersion),
                deviceName)));
    }

    @Override
    public List<DeviceOtaCreateParam> getDeviceOtaFirmware(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<DeviceOtaCreateParam> deviceOtaList = new ArrayList<>();
        upgradeDTOS.forEach(upgradeDevice -> {
            boolean exist = deviceService.checkDeviceOnline(upgradeDevice.getSn());
            if (!exist) {
                throw new IllegalArgumentException("Device is offline.");
            }
            Optional<DeviceFirmwareDTO> firmwareOpt = this.getFirmware(
                    workspaceId, upgradeDevice.getDeviceName(), upgradeDevice.getProductVersion());
            if (firmwareOpt.isEmpty()) {
                throw new IllegalArgumentException("This firmware version does not exist or is not available.");
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

        EventsOutputReceiver output = eventsReceiver.getOutput();
        log.info("SN: {}, {} ===> Upgrading progress: {}",
                sn, receiver.getMethod(), output.getProgress().toString());

        if (eventsReceiver.getResult() != ResponseResult.CODE_SUCCESS) {
            log.error("SN: {}, {} ===> Error code: {}", sn, receiver.getMethod(), eventsReceiver.getResult());
        }

        DeviceDTO device = (DeviceDTO) RedisOpsUtils.get(RedisConst.DEVICE_ONLINE_PREFIX + sn);
        String childDeviceSn = device.getChildDeviceSn();
        boolean upgrade = RedisOpsUtils.getExpire(RedisConst.FIRMWARE_UPGRADING_PREFIX + sn) > 0;
        boolean childUpgrade = RedisOpsUtils.getExpire(RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn) > 0;

        // Determine whether it is the ending state, delete the update state key in redis after the job ends.
        EventsResultStatusEnum statusEnum = EventsResultStatusEnum.find(output.getStatus());
        Collection<ConcurrentWebSocketSession> sessions = webSocketManageService.getValueWithWorkspaceAndUserType(
                device.getWorkspaceId(), UserTypeEnum.WEB.getVal());
        CustomWebSocketMessage<Object> build = CustomWebSocketMessage.builder()
                .data(eventsReceiver)
                .timestamp(System.currentTimeMillis())
                .bizCode(receiver.getMethod())
                .build();
        if (upgrade) {
            if (statusEnum.getEnd()) {
                // Delete the cache after the update is complete.
                RedisOpsUtils.del(RedisConst.FIRMWARE_UPGRADING_PREFIX + sn);
            } else {
                // Update the update progress of the dock in redis.
                RedisOpsUtils.setWithExpire(
                        RedisConst.FIRMWARE_UPGRADING_PREFIX + sn, output.getProgress().getPercent(),
                        RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
            }
            eventsReceiver.setSn(sn);
            webSocketMessageService.sendBatch(sessions, build);
        }
        if (childUpgrade) {
            if (!StringUtils.hasText(eventsReceiver.getSn())) {
                eventsReceiver.setSn(childDeviceSn);
                webSocketMessageService.sendBatch(sessions, build);
            }
            if (statusEnum.getEnd()) {
                RedisOpsUtils.del(RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn);
            } else {
                // Update the update progress of the drone in redis.
                RedisOpsUtils.setWithExpire(
                        RedisConst.FIRMWARE_UPGRADING_PREFIX + childDeviceSn, output.getProgress().getPercent(),
                        RedisConst.DEVICE_ALIVE_SECOND * RedisConst.DEVICE_ALIVE_SECOND);
            }
        }

        if (receiver.getNeedReply() != null && receiver.getNeedReply() == 1) {
            String replyTopic = headers.get(MqttHeaders.RECEIVED_TOPIC) + TopicConst._REPLY_SUF;
            messageSenderService.publish(replyTopic,
                    CommonTopicResponse.builder()
                            .tid(receiver.getTid())
                            .bid(receiver.getBid())
                            .method(receiver.getMethod())
                            .timestamp(System.currentTimeMillis())
                            .data(RequestsReply.success())
                            .build());
        }
    }

    @Override
    public Boolean checkFileExist(String workspaceId, String fileMd5) {
        return RedisOpsUtils.checkExist(RedisConst.FILE_UPLOADING_PREFIX + workspaceId + fileMd5) ||
                mapper.selectCount(new LambdaQueryWrapper<DeviceFirmwareEntity>()
                    .eq(DeviceFirmwareEntity::getWorkspaceId, workspaceId)
                    .eq(DeviceFirmwareEntity::getFileMd5, fileMd5))
                > 0;
    }

    @Override
    public PaginationData<DeviceFirmwareDTO> getAllFirmwarePagination(String workspaceId, DeviceFirmwareQueryParam param) {
        Page<DeviceFirmwareEntity> page = mapper.selectPage(new Page<>(param.getPage(), param.getPageSize()),
                new LambdaQueryWrapper<DeviceFirmwareEntity>()
                        .eq(DeviceFirmwareEntity::getWorkspaceId, workspaceId)
                        .eq(Objects.nonNull(param.getStatus()), DeviceFirmwareEntity::getStatus, param.getStatus())
                        .like(StringUtils.hasText(param.getProductVersion()), DeviceFirmwareEntity::getFirmwareVersion, param.getProductVersion())
                        .orderByDesc(DeviceFirmwareEntity::getReleaseDate), param.getDeviceName());

        List<DeviceFirmwareDTO> data = page.getRecords().stream().map(this::entity2Dto).collect(Collectors.toList());
        return new PaginationData<DeviceFirmwareDTO>(data, new Pagination(page));
    }


    @Override
    public void importFirmwareFile(String workspaceId, String creator, DeviceFirmwareUploadParam param, MultipartFile file) {
        String key = RedisConst.FILE_UPLOADING_PREFIX + workspaceId;
        String existKey = key + file.getOriginalFilename();
        if (RedisOpsUtils.getExpire(existKey) > 0) {
            throw new RuntimeException("Please try again later.");
        }
        RedisOpsUtils.setWithExpire(existKey, true, RedisConst.DEVICE_ALIVE_SECOND);
        try (InputStream is = file.getInputStream()) {
            long size = is.available();
            String md5 = DigestUtils.md5DigestAsHex(is);
            key += md5;
            boolean exist = checkFileExist(workspaceId, md5);
            if (exist) {
                throw new RuntimeException("The file already exists.");
            }
            RedisOpsUtils.set(key, System.currentTimeMillis());
            Optional<DeviceFirmwareDTO> firmwareOpt = verifyFirmwareFile(file);
            if (firmwareOpt.isEmpty()) {
                throw new RuntimeException("The file format is incorrect.");
            }

            String firmwareId = UUID.randomUUID().toString();
            String objectKey = OssConfiguration.objectDirPrefix + File.separator + firmwareId + FirmwareFileProperties.FIRMWARE_FILE_SUFFIX;

            ossServiceContext.putObject(OssConfiguration.bucket, objectKey, file.getInputStream());
            log.info("upload success. {}", file.getOriginalFilename());
            DeviceFirmwareDTO firmware = DeviceFirmwareDTO.builder()
                    .releaseNote(param.getReleaseNote())
                    .firmwareStatus(param.getStatus())
                    .fileMd5(md5)
                    .objectKey(objectKey)
                    .fileName(file.getOriginalFilename())
                    .workspaceId(workspaceId)
                    .username(creator)
                    .fileSize(size)
                    .productVersion(firmwareOpt.get().getProductVersion())
                    .releasedTime(firmwareOpt.get().getReleasedTime())
                    .firmwareId(firmwareId)
                    .build();

            saveFirmwareInfo(firmware, param.getDeviceName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RedisOpsUtils.del(key);
        }
    }

    @Override
    public void saveFirmwareInfo(DeviceFirmwareDTO firmware, List<String> deviceNames) {
        DeviceFirmwareEntity entity = dto2Entity(firmware);
        mapper.insert(entity);
        firmwareModelService.saveFirmwareDeviceName(
                FirmwareModelDTO.builder().firmwareId(entity.getFirmwareId()).deviceNames(deviceNames).build());
    }

    @Override
    public void updateFirmwareInfo(DeviceFirmwareDTO firmware) {
        mapper.update(dto2Entity(firmware),
                new LambdaUpdateWrapper<DeviceFirmwareEntity>()
                        .eq(DeviceFirmwareEntity::getFirmwareId, firmware.getFirmwareId()));
    }

    /**
     * Parse firmware file information.
     * @param file
     * @return
     */
    private Optional<DeviceFirmwareDTO> verifyFirmwareFile(MultipartFile file) {
        try (ZipInputStream unzipFile = new ZipInputStream(file.getInputStream(), StandardCharsets.UTF_8)) {
            ZipEntry nextEntry = unzipFile.getNextEntry();
            while (Objects.nonNull(nextEntry)) {
                String configName = nextEntry.getName();
                if (!configName.contains(File.separator) && configName.endsWith(FirmwareFileProperties.FIRMWARE_CONFIG_FILE_SUFFIX + FirmwareFileProperties.FIRMWARE_SIG_FILE_SUFFIX)) {
                    String[] filenameArr = configName.split(FirmwareFileProperties.FIRMWARE_FILE_DELIMITER);
                    String date = filenameArr[FirmwareFileProperties.FILENAME_RELEASE_DATE_INDEX];
                    int index = date.indexOf(".");
                    if (index != -1) {
                        date = date.substring(0, index);
                    }
                    return Optional.of(DeviceFirmwareDTO.builder()
                            .releasedTime(LocalDate.parse(
                                    date,
                                    DateTimeFormatter.ofPattern(FirmwareFileProperties.FILENAME_RELEASE_DATE_FORMAT)))
                            // delete the string v.
                            .productVersion(filenameArr[FirmwareFileProperties.FILENAME_VERSION_INDEX].substring(1))
                            .build());
                }
                nextEntry = unzipFile.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private DeviceFirmwareEntity dto2Entity(DeviceFirmwareDTO dto) {
        if (dto == null) {
            return null;
        }
        return DeviceFirmwareEntity.builder()
                .fileName(dto.getFileName())
                .fileMd5(dto.getFileMd5())
                .fileSize(dto.getFileSize())
                .firmwareId(dto.getFirmwareId())
                .firmwareVersion(dto.getProductVersion())
                .objectKey(dto.getObjectKey())
                .releaseDate(Objects.nonNull(dto.getReleasedTime()) ?
                        dto.getReleasedTime().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .releaseNote(dto.getReleaseNote())
                .status(dto.getFirmwareStatus())
                .workspaceId(dto.getWorkspaceId())
                .username(dto.getUsername())
                .build();
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
                .deviceName(Arrays.asList(entity.getDeviceName().split(",")))
                .fileMd5(entity.getFileMd5())
                .fileSize(entity.getFileSize())
                .objectKey(entity.getObjectKey())
                .firmwareId(entity.getFirmwareId())
                .fileName(entity.getFileName())
                .productVersion(entity.getFirmwareVersion())
                .releasedTime(LocalDate.ofInstant(Instant.ofEpochMilli(entity.getReleaseDate()), ZoneId.systemDefault()))
                .releaseNote(entity.getReleaseNote())
                .firmwareStatus(entity.getStatus())
                .workspaceId(entity.getWorkspaceId())
                .username(entity.getUsername())
                .build();
    }

    private DeviceOtaCreateParam dto2OtaCreateDto(DeviceFirmwareDTO dto) {
        if (dto == null) {
            return null;
        }
        return DeviceOtaCreateParam.builder()
                .fileSize(dto.getFileSize())
                .fileUrl(ossServiceContext.getObjectUrl(OssConfiguration.bucket, dto.getObjectKey()).toString())
                .fileName(dto.getFileName())
                .md5(dto.getFileMd5())
                .productVersion(dto.getProductVersion())
                .build();
    }
}
