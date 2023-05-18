package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.common.model.Pagination;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.*;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttTopicService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.component.websocket.service.IWebSocketManageService;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.manage.dao.IDeviceMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceEntity;
import com.dji.sample.manage.model.enums.*;
import com.dji.sample.manage.model.param.DeviceOtaCreateParam;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.*;
import com.dji.sample.manage.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dji.sample.component.mqtt.model.TopicConst.*;

/**
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
@Service
@Slf4j
@Transactional
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private IMessageSenderService messageSender;

    @Autowired
    private IDeviceMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private IMqttTopicService topicService;

    @Autowired
    private IWorkspaceService workspaceService;

    @Autowired
    private IDevicePayloadService payloadService;

    @Autowired
    private ISendMessageService sendMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Autowired
    private IDeviceFirmwareService deviceFirmwareService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    @Qualifier("gatewayOSDServiceImpl")
    private ITSAService tsaService;

    private static final List<String> INIT_TOPICS_SUFFIX = List.of(
            OSD_SUF, STATE_SUF, SERVICES_SUF + _REPLY_SUF, EVENTS_SUF, PROPERTY_SUF + SET_SUF + _REPLY_SUF);

    @Override
    public Boolean deviceOffline(StatusGatewayReceiver gateway) {
        String gatewaySn = gateway.getSn();
        this.subscribeTopicOnline(gatewaySn);

        // Only the remote controller is logged in and the aircraft is not connected.
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(gatewaySn);
        if (deviceOpt.isEmpty()) {
            Optional<DeviceDTO> gatewayOpt = this.getDeviceBySn(gatewaySn);
            if (gatewayOpt.isPresent()) {
                DeviceDTO value = gatewayOpt.get();
                value.setChildDeviceSn(null);
                deviceRedisService.setDeviceOnline(value);
                this.pushDeviceOnlineTopo(value.getWorkspaceId(), gatewaySn, gatewaySn);
                return true;
            }

            // When connecting for the first time
            DeviceEntity gatewayDevice = deviceGatewayConvertToDeviceEntity(gateway);
            return onlineSaveDevice(gatewayDevice, null, null).isPresent();
        }

        DeviceDTO deviceDTO = deviceOpt.get();
        String deviceSn = deviceDTO.getChildDeviceSn();
        if (!StringUtils.hasText(deviceSn)) {
            return true;
        }

        return subDeviceOffline(deviceSn);
    }

    @Override
    public Boolean subDeviceOffline(String deviceSn) {

        // If no information about this device exists in the cache, the drone is considered to be offline.
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isEmpty()) {
            log.debug("The drone is already offline.");
            return true;
        }
        DeviceDTO device = deviceOpt.get();
        // Cancel drone-related subscriptions.
        this.unsubscribeTopicOffline(deviceSn);

        capacityCameraService.deleteCapacityCameraByDeviceSn(deviceSn);
        deviceRedisService.delDeviceOnline(deviceSn);
        RedisOpsUtils.del(RedisConst.OSD_PREFIX + deviceSn);
        deviceRedisService.delHmsKeysBySn(deviceSn);
        // Publish the latest device topology information in the current workspace.
        this.pushDeviceOfflineTopo(device.getWorkspaceId(), deviceSn);

        log.debug("{} offline.", deviceSn);
        return true;
    }

    @Override
    public Boolean deviceOnline(StatusGatewayReceiver deviceGateway) {
        String deviceSn = deviceGateway.getSubDevices().get(0).getSn();

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        Optional<DeviceDTO> gatewayOpt = deviceRedisService.getDeviceOnline(deviceGateway.getSn());

        if (deviceOpt.isPresent() && gatewayOpt.isPresent()) {
            DeviceDTO device = DeviceDTO.builder().loginTime(LocalDateTime.now()).deviceSn(deviceSn).build();
            DeviceDTO gateway = DeviceDTO.builder()
                    .loginTime(LocalDateTime.now())
                    .deviceSn(deviceGateway.getSn())
                    .childDeviceSn(deviceSn).build();
            this.updateDevice(gateway);
            this.updateDevice(device);
            String workspaceId = deviceOpt.get().getWorkspaceId();
            if (StringUtils.hasText(workspaceId)) {
                this.subscribeTopicOnline(deviceSn);
                this.subscribeTopicOnline(deviceGateway.getSn());
            }
            log.warn("{} is already online.", deviceSn);
            return true;
        }

        List<DeviceDTO> gatewaysList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .childSn(deviceSn)
                        .build());
        gatewaysList.stream()
                .filter(gateway -> !gateway.getDeviceSn().equals(deviceGateway.getSn()))
                .findAny()
                .ifPresent(gateway -> {
                    gateway.setChildDeviceSn("");
                    this.updateDevice(gateway);
                });

        DeviceEntity gateway = deviceGatewayConvertToDeviceEntity(deviceGateway);
        Optional<DeviceEntity> gatewayEntityOpt = onlineSaveDevice(gateway, deviceSn, null);
        if (gatewayEntityOpt.isEmpty()) {
            log.error("Failed to go online, please check the status data or code logic.");
            return false;
        }

        DeviceEntity subDevice = subDeviceConvertToDeviceEntity(deviceGateway.getSubDevices().get(0));
        Optional<DeviceEntity> subDeviceEntityOpt = onlineSaveDevice(subDevice, null, gateway.getDeviceSn());
        if (subDeviceEntityOpt.isEmpty()) {
            log.error("Failed to go online, please check the status data or code logic.");
            return false;
        }

        subDevice = subDeviceEntityOpt.get();
        gateway = gatewayEntityOpt.get();

        // dock go online
        if (DeviceDomainEnum.DOCK.getVal() == deviceGateway.getDomain() && !subDevice.getBoundStatus()) {
            // Directly bind the drone of the dock to the same workspace as the dock.
            bindDevice(DeviceDTO.builder().deviceSn(deviceSn).workspaceId(gateway.getWorkspaceId()).build());
            subDevice.setWorkspaceId(gateway.getWorkspaceId());
        }

        // Subscribe to topic related to drone devices.
        this.subscribeTopicOnline(deviceGateway.getSn());
        this.subscribeTopicOnline(deviceSn);
        this.pushDeviceOnlineTopo(subDevice.getWorkspaceId(), deviceGateway.getSn(), deviceSn);

        log.debug("{} online.", subDevice.getDeviceSn());
        return true;
    }

    @Override
    public void subscribeTopicOnline(String sn) {
        String[] subscribedTopic = topicService.getSubscribedTopic();
        for (String s : subscribedTopic) {
            // If you have already subscribed to the topic of the device, you do not need to subscribe again.
            if (s.contains(sn)) {
                return;
            }
        }
        String prefix = THING_MODEL_PRE + PRODUCT + sn;
        INIT_TOPICS_SUFFIX.forEach(suffix -> topicService.subscribe(prefix + suffix));
    }

    @Override
    public void unsubscribeTopicOffline(String sn) {
        String prefix = THING_MODEL_PRE + PRODUCT + sn;
        INIT_TOPICS_SUFFIX.forEach(suffix -> topicService.unsubscribe(prefix + suffix));
    }

    @Override
    public Boolean delDeviceByDeviceSns(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        return mapper.delete(new LambdaQueryWrapper<DeviceEntity>()
                .in(DeviceEntity::getDeviceSn, ids))
                > 0;
    }

    @Override
    public void publishStatusReply(String sn, CommonTopicResponse<Object> response) {
        Map<String, Integer> result = new ConcurrentHashMap<>(1);
        result.put("result", 0);
        response.setData(result);

        messageSender.publish(
                new StringBuilder()
                        .append(BASIC_PRE)
                        .append(PRODUCT)
                        .append(sn)
                        .append(STATUS_SUF)
                        .append(_REPLY_SUF)
                        .toString(),
                response);
    }

    @Override
    public List<DeviceDTO> getDevicesByParams(DeviceQueryParam param) {
        return mapper.selectList(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(StringUtils.hasText(param.getDeviceSn()),
                                DeviceEntity::getDeviceSn, param.getDeviceSn())
                        .eq(param.getDeviceType() != null,
                                DeviceEntity::getDeviceType, param.getDeviceType())
                        .eq(param.getSubType() != null,
                                DeviceEntity::getSubType, param.getSubType())
                        .eq(StringUtils.hasText(param.getChildSn()),
                                DeviceEntity::getChildSn, param.getChildSn())
                        .and(!CollectionUtils.isEmpty(param.getDomains()), wrapper -> {
                            for (Integer domain : param.getDomains()) {
                                wrapper.eq(DeviceEntity::getDomain, domain).or();
                            }
                        })
                        .eq(StringUtils.hasText(param.getWorkspaceId()),
                                DeviceEntity::getWorkspaceId, param.getWorkspaceId())
                        .eq(param.getBoundStatus() != null, DeviceEntity::getBoundStatus, param.getBoundStatus())
                        .orderBy(param.isOrderBy(),
                                param.isAsc(), DeviceEntity::getId))
                .stream()
                .map(this::deviceEntityConvertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDTO> getDevicesTopoForWeb(String workspaceId) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.GATEWAY.getVal(), DeviceDomainEnum.DOCK.getVal()))
                        .build());

        devicesList.stream()
                .filter(gateway -> DeviceDomainEnum.DOCK.getVal() == gateway.getDomain() ||
                        deviceRedisService.checkDeviceOnline(gateway.getDeviceSn()))
                .forEach(this::spliceDeviceTopo);

        return devicesList;
    }

    @Override
    public void spliceDeviceTopo(DeviceDTO gateway) {

        gateway.setStatus(deviceRedisService.checkDeviceOnline(gateway.getDeviceSn()));

        // sub device
        if (!StringUtils.hasText(gateway.getChildDeviceSn())) {
            return;
        }

        DeviceDTO subDevice = getDevicesByParams(DeviceQueryParam.builder().deviceSn(gateway.getChildDeviceSn()).build()).get(0);
        subDevice.setStatus(deviceRedisService.checkDeviceOnline(subDevice.getDeviceSn()));
        gateway.setChildren(subDevice);

        // payloads
        subDevice.setPayloadsList(payloadService.getDevicePayloadEntitiesByDeviceSn(gateway.getChildDeviceSn()));
    }

    @Override
    public Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn) {
        if (sn.isBlank()) {
            return Optional.empty();
        }
        List<TopologyDeviceDTO> topologyDeviceList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(sn)
                        .build())
                .stream()
                .map(this::deviceConvertToTopologyDTO)
                .collect(Collectors.toList());
        if (topologyDeviceList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(topologyDeviceList.get(0));
    }

    @Override
    public void pushDeviceOnlineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn, String gatewaySn) {

        CustomWebSocketMessage<TopologyDeviceDTO> pilotMessage =
                CustomWebSocketMessage.<TopologyDeviceDTO>builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_ONLINE.getCode())
                        .data(new TopologyDeviceDTO())
                        .build();

        this.getDeviceTopoForPilot(sn)
                .ifPresent(pilotMessage::setData);
        pilotMessage.getData().setOnlineStatus(deviceRedisService.checkDeviceOnline(sn));
        pilotMessage.getData().setGatewaySn(gatewaySn.equals(sn) ? "" : gatewaySn);

        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    @Override
    public TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device) {
        TopologyDeviceDTO.TopologyDeviceDTOBuilder builder = TopologyDeviceDTO.builder();

        if (device != null) {
            builder.sn(device.getDeviceSn())
                    .deviceCallsign(device.getNickname())
                    .deviceModel(DeviceModelDTO.builder()
                            .domain(String.valueOf(device.getDomain()))
                            .subType(String.valueOf(device.getSubType()))
                            .type(String.valueOf(device.getType()))
                            .key(device.getDomain() + "-" + device.getType() + "-" + device.getSubType())
                            .build())
                    .iconUrls(device.getIconUrl())
                    .onlineStatus(deviceRedisService.checkDeviceOnline(device.getDeviceSn()))
                    .boundStatus(device.getBoundStatus())
                    .model(device.getDeviceName())
                    .userId(device.getUserId())
                    .domain(device.getDomain())
                    .build();
        }
        return builder.build();
    }

    @Override
    public void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn) {

        // All connected accounts in this workspace.
        Collection<ConcurrentWebSocketSession> allSessions = webSocketManageService.getValueWithWorkspace(workspaceId);

        if (!gatewaySn.equals(deviceSn)) {
            this.pushDeviceOnlineTopo(allSessions, deviceSn, gatewaySn);
            this.pushDeviceUpdateTopo(allSessions, deviceSn);
        }
        this.pushDeviceOnlineTopo(allSessions, gatewaySn, gatewaySn);
        this.pushDeviceUpdateTopo(allSessions, gatewaySn);
    }

    @Override
    public void pushDeviceOfflineTopo(String workspaceId, String sn) {
        // All connected accounts of this workspace.
        Collection<ConcurrentWebSocketSession> allSessions = webSocketManageService
                .getValueWithWorkspace(workspaceId);

        this.pushDeviceOfflineTopo(allSessions, sn);
        this.pushDeviceUpdateTopo(allSessions, sn);
    }

    @Override
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD)
    public void handleOSD(Message<?> message) {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
        byte[] payload = (byte[])message.getPayload();
        CommonTopicReceiver receiver;
        try {
            String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(),
                    topic.indexOf(OSD_SUF));

            Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(from);

            if (deviceOpt.isEmpty()) {
                deviceOpt = this.getDeviceBySn(from);
                if (deviceOpt.isEmpty()) {
                    log.error("Please restart the drone.");
                    return;
                }

                if (!StringUtils.hasText(deviceOpt.get().getWorkspaceId())) {
                    this.unsubscribeTopicOffline(from);
                    return;
                }
                deviceRedisService.setDeviceOnline(deviceOpt.get());
                this.subscribeTopicOnline(from);
            }
            DeviceDTO device = deviceOpt.get();
            deviceRedisService.setDeviceOnline(device);

            receiver = objectMapper.readValue(payload, CommonTopicReceiver.class);

            CustomWebSocketMessage<TelemetryDTO> wsMessage = CustomWebSocketMessage.<TelemetryDTO>builder()
                    .timestamp(System.currentTimeMillis())
                    .data(TelemetryDTO.builder().sn(from).build())
                    .build();

            Collection<ConcurrentWebSocketSession> webSessions = webSocketManageService
                    .getValueWithWorkspaceAndUserType(
                            device.getWorkspaceId(), UserTypeEnum.WEB.getVal());


            tsaService.handleOSD(receiver, device, webSessions, wsMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notify the pilot side that there is an update of the device topology.
     * @param sessions
     * @param deviceSn
     */
    private void pushDeviceUpdateTopo(Collection<ConcurrentWebSocketSession> sessions, String deviceSn) {

        CustomWebSocketMessage pilotMessage =
                CustomWebSocketMessage.builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_UPDATE_TOPO.getCode())
                        .data(new TopologyDeviceDTO())
                        .build();
        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    /**
     * Notify the pilot side that device is offline and needs to reacquire topology information.
     * @param sessions
     * @param sn
     */
    private void pushDeviceOfflineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn) {
        CustomWebSocketMessage<TopologyDeviceDTO> pilotMessage =
                CustomWebSocketMessage.<TopologyDeviceDTO>builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_OFFLINE.getCode())
                        .data(TopologyDeviceDTO.builder()
                                .sn(sn)
                                .onlineStatus(false)
                                .build())
                        .build();
        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    /**
     * Save the device information and update the information directly if the device already exists.
     * @param entity
     * @return
     */
    private Optional<DeviceEntity> saveDevice(DeviceEntity entity) {
        DeviceEntity deviceEntity = mapper.selectOne(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDeviceSn, entity.getDeviceSn()));
        // Update the information directly if the device already exists.
        if (deviceEntity != null) {
            if (deviceEntity.getDeviceName().equals(entity.getNickname())) {
                entity.setNickname(null);
            }
            entity.setId(deviceEntity.getId());
            mapper.updateById(entity);
            fillNullField(entity, deviceEntity);
            return Optional.of(entity);
        }
        return mapper.insert(entity) > 0 ? Optional.of(entity) : Optional.empty();
    }

    private void fillNullField(DeviceEntity entity, DeviceEntity oldEntity) {
        if (Objects.isNull(entity) || Objects.isNull(oldEntity)) {
            return;
        }
        if (Objects.isNull(entity.getWorkspaceId())) {
            entity.setWorkspaceId(oldEntity.getWorkspaceId());
        }
        if (Objects.isNull(entity.getUserId())) {
            entity.setUserId(oldEntity.getUserId());
        }
        if (Objects.isNull(entity.getChildSn())) {
            entity.setChildSn(oldEntity.getChildSn());
        }
        if (Objects.isNull(entity.getBoundStatus())) {
            entity.setBoundStatus(oldEntity.getBoundStatus());
        }
        if (Objects.isNull(entity.getBoundTime())) {
            entity.setBoundTime(oldEntity.getBoundTime());
        }
        if (Objects.isNull(entity.getFirmwareVersion())) {
            entity.setFirmwareVersion(oldEntity.getFirmwareVersion());
        }
        if (Objects.isNull(entity.getDeviceIndex())) {
            entity.setDeviceIndex(oldEntity.getDeviceIndex());
        }
    }

    /**
     * Convert the received gateway device object into a database entity object.
     * @param gateway
     * @return
     */
    private DeviceEntity deviceGatewayConvertToDeviceEntity(StatusGatewayReceiver gateway) {
        if (gateway == null) {
            return new DeviceEntity();
        }
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        // Query the model information of this gateway device.
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                .getOneDictionaryInfoByTypeSubType(Objects.nonNull(gateway.getDomain()) ?
                                gateway.getDomain() : DeviceDomainEnum.GATEWAY.getVal(),
                        gateway.getType(), gateway.getSubType());

        dictionaryOpt.ifPresent(entity ->
                builder.deviceName(entity.getDeviceName())
                        .nickname(entity.getDeviceName())
                        .deviceDesc(entity.getDeviceDesc()));

        return builder
                .deviceSn(gateway.getSn())
                .subType(gateway.getSubType())
                .deviceType(gateway.getType())
                .version(gateway.getVersion())
                .domain(gateway.getDomain() != null ?
                        gateway.getDomain() : DeviceDomainEnum.GATEWAY.getVal())
                .deviceIndex(gateway.getSubDevices().isEmpty() ? null : gateway.getSubDevices().get(0).getIndex())
                .build();
    }

    /**
     * Convert the received drone device object into a database entity object.
     * @param device
     * @return
     */
    private DeviceEntity subDeviceConvertToDeviceEntity(StatusSubDeviceReceiver device) {
        if (device == null) {
            return new DeviceEntity();
        }
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        // Query the model information of this drone device.
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                .getOneDictionaryInfoByTypeSubType(DeviceDomainEnum.SUB_DEVICE.getVal(), device.getType(), device.getSubType());

        dictionaryOpt.ifPresent(dictionary ->
                builder.deviceName(dictionary.getDeviceName())
                        .nickname(dictionary.getDeviceName())
                        .deviceDesc(dictionary.getDeviceDesc()));

        return builder
                .deviceSn(device.getSn())
                .deviceType(device.getType())
                .subType(device.getSubType())
                .version(device.getVersion())
                .domain(DeviceDomainEnum.SUB_DEVICE.getVal())
                .build();
    }

    /**
     * Convert database entity object into device data transfer object.
     * @param entity
     * @return
     */
    private DeviceDTO deviceEntityConvertToDTO(DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        DeviceDTO deviceDTO = DeviceDTO.builder()
                .deviceSn(entity.getDeviceSn())
                .childDeviceSn(entity.getChildSn())
                .deviceName(entity.getDeviceName())
                .deviceDesc(entity.getDeviceDesc())
                .controlSource(entity.getDeviceIndex())
                .workspaceId(entity.getWorkspaceId())
                .type(entity.getDeviceType())
                .subType(entity.getSubType())
                .domain(entity.getDomain())
                .iconUrl(IconUrlDTO.builder()
                        .normalUrl(entity.getUrlNormal())
                        .selectUrl(entity.getUrlSelect())
                        .build())
                .boundStatus(entity.getBoundStatus())
                .loginTime(entity.getLoginTime() != null ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getLoginTime()), ZoneId.systemDefault())
                        : null)
                .boundTime(entity.getBoundTime() != null ?
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getBoundTime()), ZoneId.systemDefault())
                        : null)
                .nickname(entity.getNickname())
                .firmwareVersion(entity.getFirmwareVersion())
                .workspaceName(entity.getWorkspaceId() != null ?
                        workspaceService.getWorkspaceByWorkspaceId(entity.getWorkspaceId())
                                .map(WorkspaceDTO::getWorkspaceName).orElse("") : "")
                .firmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal()).build();

        addFirmwareStatus(deviceDTO, entity);
        return deviceDTO;
    }

    private void addFirmwareStatus(DeviceDTO deviceDTO, DeviceEntity entity) {
        if (!StringUtils.hasText(entity.getFirmwareVersion())) {
            return;
        }
        // Query whether the device is updating firmware.
        Optional<EventsReceiver<EventsOutputProgressReceiver<FirmwareProgressExtReceiver>>> progressOpt =
                deviceRedisService.getFirmwareUpgradingProgress(entity.getDeviceSn());
        if (progressOpt.isPresent()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.UPGRADING.getVal());
            deviceDTO.setFirmwareProgress(progressOpt.map(EventsReceiver::getOutput)
                            .map(EventsOutputProgressReceiver::getProgress)
                            .map(OutputProgressReceiver::getPercent)
                            .orElse(0));
            return;
        }

        // First query the latest firmware version of the device model and compare it with the current firmware version
        // to see if it needs to be upgraded.
        Optional<DeviceFirmwareNoteDTO> firmwareReleaseNoteOpt = deviceFirmwareService.getLatestFirmwareReleaseNote(entity.getDeviceName());
        if (firmwareReleaseNoteOpt.isEmpty()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal());
            return;
        }
        if (entity.getFirmwareVersion().equals(firmwareReleaseNoteOpt.get().getProductVersion())) {
            deviceDTO.setFirmwareStatus(Objects.requireNonNullElse(entity.getCompatibleStatus(), true) ?
                    DeviceFirmwareStatusEnum.NOT_UPGRADE.getVal() :
                    DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE.getVal());
            return;
        }
        deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NORMAL_UPGRADE.getVal());
    }

    @Override
    public Boolean updateDevice(DeviceDTO deviceDTO) {
        int update = mapper.update(this.deviceDTO2Entity(deviceDTO),
                new LambdaUpdateWrapper<DeviceEntity>().eq(DeviceEntity::getDeviceSn, deviceDTO.getDeviceSn()));
        return update > 0;
    }

    @Override
    public Boolean bindDevice(DeviceDTO device) {
        device.setBoundStatus(true);
        device.setBoundTime(LocalDateTime.now());

        boolean isUpd = this.saveDevice(this.deviceDTO2Entity(device)).isPresent();
        if (!isUpd) {
            return false;
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(device.getDeviceSn());
        if (deviceOpt.isEmpty()) {
            return false;
        }

        DeviceDTO redisDevice = deviceOpt.get();
        redisDevice.setWorkspaceId(device.getWorkspaceId());
        deviceRedisService.setDeviceOnline(redisDevice);

        if (DeviceDomainEnum.GATEWAY.getVal() == redisDevice.getDomain()) {
            this.pushDeviceOnlineTopo(webSocketManageService.getValueWithWorkspace(device.getWorkspaceId()),
                    device.getDeviceSn(), device.getDeviceSn());
        }
        if (DeviceDomainEnum.SUB_DEVICE.getVal() == redisDevice.getDomain()) {
            DeviceDTO subDevice = this.getDevicesByParams(DeviceQueryParam.builder()
                    .childSn(device.getChildDeviceSn())
                    .build()).get(0);
            this.pushDeviceOnlineTopo(webSocketManageService.getValueWithWorkspace(device.getWorkspaceId()),
                    device.getDeviceSn(), subDevice.getDeviceSn());
        }
        this.subscribeTopicOnline(device.getDeviceSn());

        return true;
    }

    /**
     * Handle dock binding status requests.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS, outputChannel = ChannelName.OUTBOUND)
    public void bindStatus(CommonTopicReceiver receiver, MessageHeaders headers) {
        List<Map<String, String>> data = ((Map<String, List<Map<String, String>>>) receiver.getData()).get(MapKeyConst.DEVICES);
        String dockSn = data.get(0).get(MapKeyConst.SN);
        String droneSn = data.size() > 1 ? data.get(1).get(MapKeyConst.SN) : "null";

        Optional<DeviceDTO> dockOpt = this.getDeviceBySn(dockSn);
        Optional<DeviceDTO> droneOpt = this.getDeviceBySn(droneSn);

        List<BindStatusReceiver> bindStatusResult = new ArrayList<>();
        bindStatusResult.add(dockOpt.isPresent() ? this.dto2BindStatus(dockOpt.get()) :
                BindStatusReceiver.builder().sn(dockSn).isDeviceBindOrganization(false).build());
        if (data.size() > 1) {
            bindStatusResult.add(droneOpt.isPresent() ? this.dto2BindStatus(droneOpt.get()) :
                    BindStatusReceiver.builder().sn(droneSn).isDeviceBindOrganization(false).build());
        }

        messageSender.publish(headers.get(MqttHeaders.RECEIVED_TOPIC) + _REPLY_SUF,
                CommonTopicResponse.builder()
                        .tid(receiver.getTid())
                        .bid(receiver.getBid())
                        .timestamp(System.currentTimeMillis())
                        .method(RequestsMethodEnum.AIRPORT_BIND_STATUS.getMethod())
                        .data(RequestsReply.success(Map.of(MapKeyConst.BIND_STATUS, bindStatusResult)))
                        .build());

    }

    /**
     * Handle dock binding requests.
     * @param receiver
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND)
    public void bindDevice(CommonTopicReceiver receiver, MessageHeaders headers) {
        Map<String, List<BindDeviceReceiver>> data = objectMapper.convertValue(receiver.getData(),
                new TypeReference<Map<String, List<BindDeviceReceiver>>>() {});
        List<BindDeviceReceiver> devices = data.get(MapKeyConst.BIND_DEVICES);
        BindDeviceReceiver dock = null;
        BindDeviceReceiver drone = null;
        for (BindDeviceReceiver device : devices) {
            int val = Integer.parseInt(device.getDeviceModelKey().split("-")[0]);
            if (val == DeviceDomainEnum.DOCK.getVal()) {
                dock = device;
            }
            if (val == DeviceDomainEnum.SUB_DEVICE.getVal()) {
                drone = device;
            }
        }

        Optional<DeviceEntity> dockEntityOpt = this.bindDevice2Entity(DeviceDomainEnum.DOCK.getVal(), dock);
        Optional<DeviceEntity> droneEntityOpt = this.bindDevice2Entity(DeviceDomainEnum.SUB_DEVICE.getVal(), drone);

        List<ErrorInfoReply> bindResult = new ArrayList<>();

        droneEntityOpt.ifPresent(droneEntity -> {
            dockEntityOpt.get().setChildSn(droneEntity.getDeviceSn());
            Optional<DeviceEntity> deviceEntityOpt = this.saveDevice(droneEntity);
            bindResult.add(
                    deviceEntityOpt.isPresent() ?
                        ErrorInfoReply.success(droneEntity.getDeviceSn()) :
                        new ErrorInfoReply(droneEntity.getDeviceSn(),
                                CommonErrorEnum.DEVICE_BINDING_FAILED.getErrorCode())
            );
        });
        Optional<DeviceEntity> dockOpt = this.saveDevice(dockEntityOpt.get());

        bindResult.add(dockOpt.isPresent() ?
                ErrorInfoReply.success(dock.getSn()) :
                    new ErrorInfoReply(dock.getSn(),
                            CommonErrorEnum.DEVICE_BINDING_FAILED.getErrorCode()));

        String topic = headers.get(MqttHeaders.RECEIVED_TOPIC) + _REPLY_SUF;
        messageSender.publish(topic,
                CommonTopicResponse.builder()
                        .tid(receiver.getTid())
                        .bid(receiver.getBid())
                        .method(RequestsMethodEnum.AIRPORT_ORGANIZATION_BIND.getMethod())
                        .timestamp(System.currentTimeMillis())
                        .data(RequestsReply.success(Map.of(MapKeyConst.ERR_INFOS, bindResult)))
                        .build());

    }

    @Override
    public PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page,
                                                               Long pageSize, Integer domain) {

        Page<DeviceEntity> pagination = mapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDomain, domain)
                        .eq(DeviceEntity::getWorkspaceId, workspaceId)
                        .eq(DeviceEntity::getBoundStatus, true));
        List<DeviceDTO> devicesList = pagination.getRecords().stream().map(this::deviceEntityConvertToDTO)
                .peek(device -> {
                    device.setStatus(deviceRedisService.checkDeviceOnline(device.getDeviceSn()));
                    if (StringUtils.hasText(device.getChildDeviceSn())) {
                        Optional<DeviceDTO> childOpt = this.getDeviceBySn(device.getChildDeviceSn());
                        childOpt.ifPresent(child -> {
                            child.setStatus(deviceRedisService.checkDeviceOnline(child.getDeviceSn()));
                            child.setWorkspaceName(device.getWorkspaceName());
                            device.setChildren(child);
                        });
                    }
                })
                .collect(Collectors.toList());
        return new PaginationData<DeviceDTO>(devicesList, new Pagination(pagination));
    }

    @Override
    public void unbindDevice(String deviceSn) {

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isPresent()) {
            subDeviceOffline(deviceSn);
        } else {
            deviceOpt = getDeviceBySn(deviceSn);
        }
        if (deviceOpt.isEmpty()) {
            return;
        }
        DeviceDTO device = DeviceDTO.builder()
                .deviceSn(deviceSn)
                .workspaceId("")
                .userId("")
                .boundStatus(false)
                .build();
        this.updateDevice(device);
    }

    @Override
    public Optional<DeviceDTO> getDeviceBySn(String sn) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(DeviceQueryParam.builder().deviceSn(sn).build());
        if (devicesList.isEmpty()) {
            return Optional.empty();
        }
        DeviceDTO device = devicesList.get(0);
        device.setStatus(deviceRedisService.checkDeviceOnline(sn));
        return Optional.of(device);
    }

    /**
     * Update the firmware version information of the device or payload.
     * @param receiver
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_FIRMWARE_VERSION)
    public void updateFirmwareVersion(FirmwareVersionReceiver receiver) {
        // If the reported version is empty, it will not be processed to prevent misleading page.
        if (!StringUtils.hasText(receiver.getFirmwareVersion())) {
            return;
        }

        if (receiver.getDomain() == DeviceDomainEnum.SUB_DEVICE) {
            final DeviceDTO device = DeviceDTO.builder()
                    .deviceSn(receiver.getSn())
                    .firmwareVersion(receiver.getFirmwareVersion())
                    .firmwareStatus(receiver.getCompatibleStatus() == null ?
                            null : DeviceFirmwareStatusEnum.CompatibleStatusEnum.INCONSISTENT.getVal() != receiver.getCompatibleStatus() ?
                            DeviceFirmwareStatusEnum.UNKNOWN.getVal() : DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE.getVal())
                    .build();
            this.updateDevice(device);
            return;
        }
        payloadService.updateFirmwareVersion(receiver);
    }

    @Override
    public ResponseResult createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<DeviceOtaCreateParam> deviceOtaFirmwares = deviceFirmwareService.getDeviceOtaFirmware(workspaceId, upgradeDTOS);
        if (deviceOtaFirmwares.isEmpty()) {
            return ResponseResult.error();
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceOtaFirmwares.get(0).getSn());
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("Device is offline.");
        }
        DeviceDTO device = deviceOpt.get();
        String gatewaySn = DeviceDomainEnum.DOCK.getVal() == device.getDomain() ? device.getDeviceSn() : device.getParentSn();

        checkOtaConditions(gatewaySn);

        String bid = UUID.randomUUID().toString();
        ServiceReply serviceReply = messageSender.publishServicesTopic(
                gatewaySn, FirmwareMethodEnum.OTA_CREATE.getMethod(), Map.of(MapKeyConst.DEVICES, deviceOtaFirmwares), bid);
        if (serviceReply.getResult() != ResponseResult.CODE_SUCCESS) {
            return ResponseResult.error(serviceReply.getResult(), "Firmware Error Code: " + serviceReply.getResult());
        }

        // Record the device state that needs to be updated.
        deviceOtaFirmwares.forEach(deviceOta -> deviceRedisService.setFirmwareUpgrading(deviceOta.getSn(),
                EventsReceiver.<EventsOutputProgressReceiver<FirmwareProgressExtReceiver>>builder()
                        .bid(bid).sn(deviceOta.getSn()).build()));
        return ResponseResult.success();
    }

    /**
     * Determine whether the firmware can be upgraded.
     * @param dockSn
     */
    private void checkOtaConditions(String dockSn) {
        Optional<OsdDockReceiver> deviceOpt = deviceRedisService.getDeviceOsd(dockSn, OsdDockReceiver.class);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("Dock is offline.");
        }
        boolean emergencyStopState = deviceOpt.get().getEmergencyStopState();
        if (emergencyStopState) {
            throw new RuntimeException("The emergency stop button of the dock is pressed and can't be upgraded.");
        }

        DockModeCodeEnum dockMode = this.getDockMode(dockSn);
        if (DockModeCodeEnum.IDLE != dockMode) {
            throw new RuntimeException("The current status of the dock can't be upgraded.");
        }
    }

    @Override
    public void devicePropertySet(String workspaceId, String dockSn, DeviceSetPropertyEnum propertyEnum, JsonNode param) {
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("Dock is offline.");
        }
        String childSn = dockOpt.get().getChildDeviceSn();
        boolean deviceOnline = deviceRedisService.checkDeviceOnline(childSn);
        Optional<OsdSubDeviceReceiver> osdOpt = deviceRedisService.getDeviceOsd(childSn, OsdSubDeviceReceiver.class);
        if (!deviceOnline || osdOpt.isEmpty()) {
            throw new RuntimeException("Device is offline.");
        }

        // Make sure the data is valid.
        BasicDeviceProperty basicDeviceProperty = objectMapper.convertValue(param, propertyEnum.getClazz());
        boolean valid = basicDeviceProperty.valid();
        if (!valid) {
            throw new IllegalArgumentException(CommonErrorEnum.ILLEGAL_ARGUMENT.getErrorMsg());
        }

        String topic = THING_MODEL_PRE + PRODUCT + dockSn + PROPERTY_SUF + SET_SUF;
        if (!param.isObject()) {
            this.deviceOnePropertySet(topic, propertyEnum, Map.entry(propertyEnum.getProperty(), param));
            return;
        }
        // If there are multiple parameters, set them separately.
        for (Iterator<Map.Entry<String, JsonNode>> filed = param.fields(); filed.hasNext(); ) {
            Map.Entry<String, JsonNode> node = filed.next();
            boolean isPublish = basicDeviceProperty.canPublish(node.getKey(), osdOpt.get());
            if (!isPublish) {
                continue;
            }
            this.deviceOnePropertySet(topic, propertyEnum, Map.entry(propertyEnum.getProperty(), node));
        }

    }

    @Override
    public void deviceOnePropertySet(String topic, DeviceSetPropertyEnum propertyEnum, Map.Entry<String, Object> value) {
        if (Objects.isNull(value) || Objects.isNull(value.getValue())) {
            throw new IllegalArgumentException(CommonErrorEnum.ILLEGAL_ARGUMENT.getErrorMsg());
        }

        Map reply = messageSender.publishWithReply(
                Map.class, topic,
                CommonTopicResponse.builder()
                        .bid(UUID.randomUUID().toString())
                        .tid(UUID.randomUUID().toString())
                        .timestamp(System.currentTimeMillis())
                        .data(value)
                        .build());

        while (true) {
            reply = (Map<String, Object>) reply.get(value.getKey());
            if (value.getValue() instanceof JsonNode) {
                break;
            }
            value = (Map.Entry) value.getValue();
        }

        SetReply setReply = objectMapper.convertValue(reply, SetReply.class);
        if (SetReplyStatusResultEnum.SUCCESS.getVal() != setReply.getResult()) {
            throw new RuntimeException("Failed to set " + value.getKey() + "; Error Code: " + setReply.getResult());
        }

    }

    @Override
    public DockModeCodeEnum getDockMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDockReceiver.class)
                .map(OsdDockReceiver::getModeCode).orElse(DockModeCodeEnum.DISCONNECTED);
    }

    @Override
    public DeviceModeCodeEnum getDeviceMode(String deviceSn) {
        return deviceRedisService.getDeviceOsd(deviceSn, OsdSubDeviceReceiver.class)
                .map(OsdSubDeviceReceiver::getModeCode).orElse(DeviceModeCodeEnum.DISCONNECTED);
    }

    @Override
    public Boolean checkDockDrcMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDockReceiver.class)
                .map(OsdDockReceiver::getDrcState)
                .orElse(DockDrcStateEnum.DISCONNECTED) != DockDrcStateEnum.DISCONNECTED;
    }

    @Override
    public Boolean checkAuthorityFlight(String gatewaySn) {
        return deviceRedisService.getDeviceOnline(gatewaySn).flatMap(gateway ->
                Optional.of((DeviceDomainEnum.DOCK.getVal() == gateway.getDomain()
                        || DeviceDomainEnum.GATEWAY.getVal() == gateway.getDomain())
                    && ControlSourceEnum.A.getControlSource().equals(gateway.getControlSource())))
                .orElse(true);
    }

    /**
     * Convert device data transfer object into database entity object.
     * @param dto
     * @return
     */
    private DeviceEntity deviceDTO2Entity(DeviceDTO dto) {
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();
        if (dto == null) {
            return builder.build();
        }

        return builder.deviceSn(dto.getDeviceSn())
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .workspaceId(dto.getWorkspaceId())
                .boundStatus(dto.getBoundStatus())
                .loginTime(dto.getLoginTime() != null ?
                        dto.getLoginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .boundTime(dto.getBoundTime() != null ?
                        dto.getBoundTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .childSn(dto.getChildDeviceSn())
                .domain(dto.getDomain())
                .firmwareVersion(dto.getFirmwareVersion())
                .compatibleStatus(dto.getFirmwareStatus() == null ? null :
                        DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE != DeviceFirmwareStatusEnum.find(dto.getFirmwareStatus()))
                .build();
    }

    /**
     * Convert device binding data object into database entity object.
     *
     * @param domain
     * @param receiver
     * @return
     */
    private Optional<DeviceEntity> bindDevice2Entity(Integer domain, BindDeviceReceiver receiver) {
        if (receiver == null) {
            return Optional.empty();
        }
        int[] droneKey = Arrays.stream(receiver.getDeviceModelKey().split("-")).mapToInt(Integer::parseInt).toArray();
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService.getOneDictionaryInfoByTypeSubType(domain, droneKey[1], droneKey[2]);
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();

        dictionaryOpt.ifPresent(entity ->
                builder.deviceName(entity.getDeviceName())
                        .nickname(entity.getDeviceName())
                        .deviceDesc(entity.getDeviceDesc()));

        Optional<WorkspaceDTO> workspace = workspaceService.getWorkspaceNameByBindCode(receiver.getDeviceBindingCode());

        DeviceEntity entity = builder
                .workspaceId(workspace.isPresent() ? workspace.get().getWorkspaceId() : receiver.getOrganizationId())
                .domain(droneKey[0])
                .deviceType(droneKey[1])
                .subType(droneKey[2])
                .deviceSn(receiver.getSn())
                .boundStatus(true)
                .loginTime(System.currentTimeMillis())
                .boundTime(System.currentTimeMillis())
                .urlSelect(IconUrlEnum.SELECT_EQUIPMENT.getUrl())
                .urlNormal(IconUrlEnum.NORMAL_EQUIPMENT.getUrl())
                .build();
        if (StringUtils.hasText(receiver.getDeviceCallsign())) {
            entity.setNickname(receiver.getDeviceCallsign());
        }
        return Optional.of(entity);
    }

    /**
     * Convert device data transfer object into device binding status data object.
     * @param device
     * @return
     */
    private BindStatusReceiver dto2BindStatus(DeviceDTO device) {
        if (device == null) {
            return null;
        }
        return BindStatusReceiver.builder()
                .sn(device.getDeviceSn())
                .deviceCallsign(device.getNickname())
                .isDeviceBindOrganization(device.getBoundStatus())
                .organizationId(device.getWorkspaceId())
                .organizationName(device.getWorkspaceName())
                .build();
    }

    private Optional<DeviceEntity> onlineSaveDevice(DeviceEntity device, String childSn, String parentSn) {

        Optional<DeviceDTO> deviceOpt = this.getDeviceBySn(device.getDeviceSn());
        if (deviceOpt.isEmpty()) {
            // Set the icon of the gateway device displayed in the pilot's map, required in the TSA module.
            device.setUrlNormal(IconUrlEnum.NORMAL_PERSON.getUrl());
            // Set the icon of the gateway device displayed in the pilot's map when it is selected, required in the TSA module.
            device.setUrlSelect(IconUrlEnum.SELECT_PERSON.getUrl());
            device.setBoundStatus(false);
        } else {
            DeviceDTO oldDevice = deviceOpt.get();
            device.setNickname(oldDevice.getNickname());
            device.setBoundStatus(oldDevice.getBoundStatus());
        }

        device.setChildSn(childSn);
        device.setLoginTime(System.currentTimeMillis());

        Optional<DeviceEntity> saveDeviceOpt = this.saveDevice(device);
        if (saveDeviceOpt.isEmpty()) {
            return saveDeviceOpt;
        }

        DeviceDTO redisDevice = this.deviceEntityConvertToDTO(saveDeviceOpt.get());
        redisDevice.setParentSn(parentSn);

        deviceRedisService.setDeviceOnline(redisDevice);

        return saveDeviceOpt;
    }

    /**
     * Handles messages in the state topic about basic drone data.
     *
     * Note: Only the data of the drone payload is handled here. You can handle other data from the drone
     * according to your business needs.
     * @param deviceBasic   basic drone data
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_BASIC, outputChannel = ChannelName.INBOUND_STATE_PAYLOAD)
    public List<DevicePayloadReceiver> stateBasic(DeviceBasicReceiver deviceBasic) {
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceBasic.getDeviceSn());
        if (deviceOpt.isEmpty()) {
            return deviceBasic.getPayloads();
        }
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(deviceOpt.get().getParentSn());
        if (dockOpt.isEmpty()) {
            return deviceBasic.getPayloads();
        }
        DeviceDTO dock = dockOpt.get();
        if (!deviceBasic.getControlSource().equals(dock.getControlSource())) {
            dock.setControlSource(deviceBasic.getControlSource());
            deviceRedisService.setDeviceOnline(dock);

            sendMessageService.sendBatch(dock.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                    BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                    DeviceAuthorityDTO.builder()
                            .controlSource(dock.getControlSource())
                            .sn(dock.getDeviceSn())
                            .type(DroneAuthorityEnum.FLIGHT)
                            .build());

        }
        return deviceBasic.getPayloads();
    }
}