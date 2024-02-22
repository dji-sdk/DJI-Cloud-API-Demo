package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.component.mqtt.model.EventsReceiver;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.manage.dao.IDeviceMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceEntity;
import com.dji.sample.manage.model.enums.DeviceFirmwareStatusEnum;
import com.dji.sample.manage.model.enums.PropertySetFieldEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.BasicDeviceProperty;
import com.dji.sample.manage.service.*;
import com.dji.sdk.cloudapi.device.*;
import com.dji.sdk.cloudapi.firmware.*;
import com.dji.sdk.cloudapi.firmware.api.AbstractFirmwareService;
import com.dji.sdk.cloudapi.property.api.AbstractPropertyService;
import com.dji.sdk.cloudapi.tsa.DeviceIconUrl;
import com.dji.sdk.cloudapi.tsa.TopologyDeviceModel;
import com.dji.sdk.common.*;
import com.dji.sdk.config.version.GatewayManager;
import com.dji.sdk.exception.CloudSDKException;
import com.dji.sdk.mqtt.IMqttTopicService;
import com.dji.sdk.mqtt.MqttGatewayPublish;
import com.dji.sdk.mqtt.events.EventsSubscribe;
import com.dji.sdk.mqtt.osd.OsdSubscribe;
import com.dji.sdk.mqtt.property.PropertySetReplyResultEnum;
import com.dji.sdk.mqtt.property.PropertySetSubscribe;
import com.dji.sdk.mqtt.requests.RequestsSubscribe;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.ServicesSubscribe;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.dji.sdk.mqtt.state.StateSubscribe;
import com.dji.sdk.mqtt.status.StatusSubscribe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private MqttGatewayPublish messageSender;

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
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDeviceFirmwareService deviceFirmwareService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private StatusSubscribe statusSubscribe;

    @Autowired
    private StateSubscribe stateSubscribe;

    @Autowired
    private OsdSubscribe osdSubscribe;

    @Autowired
    private ServicesSubscribe servicesSubscribe;

    @Autowired
    private EventsSubscribe eventsSubscribe;

    @Autowired
    private RequestsSubscribe requestsSubscribe;

    @Autowired
    private PropertySetSubscribe propertySetSubscribe;

    @Autowired
    private AbstractPropertyService abstractPropertyService;

    @Autowired
    private AbstractFirmwareService abstractFirmwareService;

    @Override
    public void subDeviceOffline(String deviceSn) {
        // If no information about this device exists in the cache, the drone is considered to be offline.
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isEmpty()) {
            log.debug("The drone is already offline.");
            return;
        }
        try {
            gatewayOnlineSubscribeTopic(SDKManager.getDeviceSDK(String.valueOf(deviceOpt.get().getParentSn())));
        } catch (CloudSDKException e) {
            log.debug("The gateway is already offline.", e);
        }
        deviceRedisService.subDeviceOffline(deviceSn);
        // Publish the latest device topology information in the current workspace.
        pushDeviceOfflineTopo(deviceOpt.get().getWorkspaceId(), deviceSn);
        log.debug("{} offline.", deviceSn);
    }

    @Override
    public void gatewayOffline(String gatewaySn) {
        // If no information about this device exists in the cache, the drone is considered to be offline.
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(gatewaySn);
        if (deviceOpt.isEmpty()) {
            log.debug("The gateway is already offline.");
            return;
        }

        deviceRedisService.subDeviceOffline(deviceOpt.get().getChildDeviceSn());
        deviceRedisService.gatewayOffline(gatewaySn);
        offlineUnsubscribeTopic(SDKManager.getDeviceSDK(gatewaySn));
        // Publish the latest device topology information in the current workspace.
        pushDeviceOfflineTopo(deviceOpt.get().getWorkspaceId(), gatewaySn);
        log.debug("{} offline.", gatewaySn);
    }

    @Override
    public void gatewayOnlineSubscribeTopic(GatewayManager gateway) {
        statusSubscribe.subscribe(gateway);
        stateSubscribe.subscribe(gateway, true);
        osdSubscribe.subscribe(gateway, true);
        servicesSubscribe.subscribe(gateway);
        eventsSubscribe.subscribe(gateway, true);
        requestsSubscribe.subscribe(gateway);
        propertySetSubscribe.subscribe(gateway);
    }

    @Override
    public void subDeviceOnlineSubscribeTopic(GatewayManager gateway) {
        statusSubscribe.subscribe(gateway);
        stateSubscribe.subscribe(gateway, false);
        osdSubscribe.subscribe(gateway, false);
        servicesSubscribe.subscribe(gateway);
        eventsSubscribe.subscribe(gateway, false);
        requestsSubscribe.subscribe(gateway);
        propertySetSubscribe.subscribe(gateway);
    }

    @Override
    public void offlineUnsubscribeTopic(GatewayManager gateway) {
        statusSubscribe.unsubscribe(gateway);
        stateSubscribe.unsubscribe(gateway);
        osdSubscribe.unsubscribe(gateway);
        servicesSubscribe.unsubscribe(gateway);
        eventsSubscribe.unsubscribe(gateway);
        requestsSubscribe.unsubscribe(gateway);
        propertySetSubscribe.unsubscribe(gateway);
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
                        .domains(List.of(DeviceDomainEnum.REMOTER_CONTROL.getDomain(), DeviceDomainEnum.DOCK.getDomain()))
                        .build());

        devicesList.stream()
                .filter(gateway -> DeviceDomainEnum.DOCK == gateway.getDomain() ||
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
        if (!StringUtils.hasText(sn)) {
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
    public TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device) {
        if (device == null) {
            return null;
        }
        return new TopologyDeviceDTO()
                    .setSn(device.getDeviceSn())
                    .setDeviceCallsign(device.getNickname())
                    .setDeviceModel(new TopologyDeviceModel()
                            .setDomain(device.getDomain())
                            .setSubType(device.getSubType())
                            .setType(device.getType())
                            .setDeviceModelKey(DeviceEnum.find(device.getDomain(), device.getType(), device.getSubType())))
                    .setIconUrls(device.getIconUrl())
                    .setOnlineStatus(deviceRedisService.checkDeviceOnline(device.getDeviceSn()))
                    .setUserCallsign(device.getNickname())
                    .setBoundStatus(device.getBoundStatus())
                    .setModel(device.getDeviceName())
                    .setUserId(device.getUserId())
                    .setDomain(device.getDomain())
                    .setGatewaySn(device.getParentSn());
    }

    @Override
    public void pushDeviceOfflineTopo(String workspaceId, String deviceSn) {
        webSocketMessageService.sendBatch(
                workspaceId, null, com.dji.sdk.websocket.BizCodeEnum.DEVICE_OFFLINE.getCode(),
                new TopologyDeviceDTO().setSn(deviceSn).setOnlineStatus(false));
    }

    @Override
    public void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn) {
        webSocketMessageService.sendBatch(
                workspaceId, null, com.dji.sdk.websocket.BizCodeEnum.DEVICE_ONLINE.getCode(),
                getDeviceTopoForPilot(deviceSn).orElseGet(TopologyDeviceDTO::new).setGatewaySn(gatewaySn));
    }

    @Override
    public void pushOsdDataToPilot(String workspaceId, String sn, DeviceOsdHost data) {
        webSocketMessageService.sendBatch(
                workspaceId, UserTypeEnum.PILOT.getVal(), com.dji.sdk.websocket.BizCodeEnum.DEVICE_OSD.getCode(),
                new DeviceOsdWsResponse()
                        .setSn(sn)
                        .setHost(data));
    }

    @Override
    public void pushOsdDataToWeb(String workspaceId, BizCodeEnum codeEnum, String sn, Object data) {
        webSocketMessageService.sendBatch(
                workspaceId, UserTypeEnum.WEB.getVal(), codeEnum.getCode(), TelemetryDTO.builder().sn(sn).host(data).build());
    }

    /**
     * Save the device information and update the information directly if the device already exists.
     * @param device
     * @return
     */
    public Boolean saveOrUpdateDevice(DeviceDTO device) {
        int count = mapper.selectCount(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDeviceSn, device.getDeviceSn()));
        return count > 0 ? updateDevice(device) : saveDevice(device) > 0;
    }

    /**
     * Save the device information.
     * @param device
     * @return
     */
    public Integer saveDevice(DeviceDTO device) {
        DeviceEntity entity = deviceDTO2Entity(device);
        return mapper.insert(entity) > 0 ? entity.getId() : -1;
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
        DeviceDTO.DeviceDTOBuilder builder = DeviceDTO.builder();
        try {
            builder
                    .deviceSn(entity.getDeviceSn())
                    .childDeviceSn(entity.getChildSn())
                    .deviceName(entity.getDeviceName())
                    .deviceDesc(entity.getDeviceDesc())
                    .controlSource(ControlSourceEnum.find(entity.getDeviceIndex()))
                    .workspaceId(entity.getWorkspaceId())
                    .type(DeviceTypeEnum.find(entity.getDeviceType()))
                    .subType(DeviceSubTypeEnum.find(entity.getSubType()))
                    .domain(DeviceDomainEnum.find(entity.getDomain()))
                    .iconUrl(new DeviceIconUrl()
                            .setNormalIconUrl(entity.getUrlNormal())
                            .setSelectIconUrl(entity.getUrlSelect()))
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
                    .firmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE)
                    .thingVersion(entity.getVersion()).build();
        } catch (CloudSDKException e) {
            log.error(e.getLocalizedMessage() + "Entity: {}", entity);
        }
        DeviceDTO deviceDTO = builder.build();
        addFirmwareStatus(deviceDTO, entity);
        return deviceDTO;
    }

    private void addFirmwareStatus(DeviceDTO deviceDTO, DeviceEntity entity) {
        if (!StringUtils.hasText(entity.getFirmwareVersion())) {
            return;
        }
        // Query whether the device is updating firmware.
        Optional<EventsReceiver<OtaProgress>> progressOpt = deviceRedisService.getFirmwareUpgradingProgress(entity.getDeviceSn());
        if (progressOpt.isPresent()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.UPGRADING);
            deviceDTO.setFirmwareProgress(progressOpt.map(EventsReceiver::getOutput)
                            .map(OtaProgress::getProgress)
                            .map(OtaProgressData::getPercent)
                            .orElse(0));
            return;
        }

        // First query the latest firmware version of the device model and compare it with the current firmware version
        // to see if it needs to be upgraded.
        Optional<DeviceFirmwareNoteDTO> firmwareReleaseNoteOpt = deviceFirmwareService.getLatestFirmwareReleaseNote(entity.getDeviceName());
        if (firmwareReleaseNoteOpt.isEmpty()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE);
            return;
        }
        if (entity.getFirmwareVersion().equals(firmwareReleaseNoteOpt.get().getProductVersion())) {
            deviceDTO.setFirmwareStatus(entity.getCompatibleStatus() ?
                    DeviceFirmwareStatusEnum.NOT_UPGRADE :
                    DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE);
            return;
        }
        deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NORMAL_UPGRADE);
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

        boolean isUpd = this.updateDevice(device);
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

        String gatewaySn, deviceSn;
        if (DeviceDomainEnum.REMOTER_CONTROL == redisDevice.getDomain()) {
            gatewaySn = device.getDeviceSn();
            deviceSn = redisDevice.getChildDeviceSn();
        } else {
            gatewaySn = redisDevice.getParentSn();
            deviceSn = device.getDeviceSn();
        }

        pushDeviceOnlineTopo(device.getWorkspaceId(), gatewaySn, deviceSn);
        subDeviceOnlineSubscribeTopic(SDKManager.getDeviceSDK(gatewaySn));
        return true;
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
        return new PaginationData<DeviceDTO>(devicesList, new Pagination(pagination.getCurrent(), pagination.getSize(), pagination.getTotal()));
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

    @Override
    public HttpResultResponse createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<OtaCreateDevice> deviceOtaFirmwares = deviceFirmwareService.getDeviceOtaFirmware(workspaceId, upgradeDTOS);
        if (deviceOtaFirmwares.isEmpty()) {
            return HttpResultResponse.error();
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceOtaFirmwares.get(0).getSn());
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("Device is offline.");
        }
        DeviceDTO device = deviceOpt.get();
        String gatewaySn = DeviceDomainEnum.DOCK == device.getDomain() ? device.getDeviceSn() : device.getParentSn();

        checkOtaConditions(gatewaySn);

        TopicServicesResponse<ServicesReplyData<OtaCreateResponse>> response = abstractFirmwareService.otaCreate(
                SDKManager.getDeviceSDK(gatewaySn), new OtaCreateRequest().setDevices(deviceOtaFirmwares));
        ServicesReplyData<OtaCreateResponse> serviceReply = response.getData();
        String bid = response.getBid();
        if (!serviceReply.getResult().isSuccess()) {
            return HttpResultResponse.error(serviceReply.getResult());
        }

        // Record the device state that needs to be updated.
        deviceOtaFirmwares.forEach(deviceOta -> deviceRedisService.setFirmwareUpgrading(deviceOta.getSn(),
                EventsReceiver.<OtaProgress>builder().bid(bid).sn(deviceOta.getSn()).build()));
        return HttpResultResponse.success();
    }

    /**
     * Determine whether the firmware can be upgraded.
     * @param dockSn
     */
    private void checkOtaConditions(String dockSn) {
        Optional<OsdDock> deviceOpt = deviceRedisService.getDeviceOsd(dockSn, OsdDock.class);
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
    public int devicePropertySet(String workspaceId, String dockSn, JsonNode param) {
        String property = param.fieldNames().next();
        PropertySetFieldEnum propertyEnum = PropertySetFieldEnum.find(property);

        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("Dock is offline.");
        }
        String childSn = dockOpt.get().getChildDeviceSn();
        Optional<OsdDockDrone> osdOpt = deviceRedisService.getDeviceOsd(childSn, OsdDockDrone.class);
        if (osdOpt.isEmpty()) {
            throw new RuntimeException("Device is offline.");
        }

        // Make sure the data is valid.
        BasicDeviceProperty basicDeviceProperty = objectMapper.convertValue(param.get(property), propertyEnum.getClazz());
        boolean valid = basicDeviceProperty.valid();
        if (!valid) {
            throw new IllegalArgumentException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        boolean isPublish = basicDeviceProperty.canPublish(osdOpt.get());
        if (!isPublish) {
            return PropertySetReplyResultEnum.SUCCESS.getResult();
        }
        BaseModel baseModel = objectMapper.convertValue(param, propertyEnum.getProperty().getClazz());
        PropertySetReplyResultEnum result = abstractPropertyService.propertySet(
                SDKManager.getDeviceSDK(dockSn), propertyEnum.getProperty(), baseModel);
        return result.getResult();
    }

    @Override
    public DockModeCodeEnum getDockMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDock.class)
                .map(OsdDock::getModeCode).orElse(null);
    }

    @Override
    public DroneModeCodeEnum getDeviceMode(String deviceSn) {
        return deviceRedisService.getDeviceOsd(deviceSn, OsdDockDrone.class)
                .map(OsdDockDrone::getModeCode).orElse(DroneModeCodeEnum.DISCONNECTED);
    }

    @Override
    public Boolean checkDockDrcMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDock.class)
                .map(OsdDock::getDrcState)
                .orElse(DrcStateEnum.DISCONNECTED) != DrcStateEnum.DISCONNECTED;
    }

    @Override
    public Boolean checkAuthorityFlight(String gatewaySn) {
        return deviceRedisService.getDeviceOnline(gatewaySn).flatMap(gateway ->
                Optional.of((DeviceDomainEnum.DOCK == gateway.getDomain()
                        || DeviceDomainEnum.REMOTER_CONTROL == gateway.getDomain())
                    && ControlSourceEnum.A == gateway.getControlSource()))
                .orElse(true);
    }

    @Override
    public void updateFlightControl(DeviceDTO gateway, ControlSourceEnum controlSource) {
        if (controlSource == gateway.getControlSource()) {
            return;
        }
        gateway.setControlSource(controlSource);
        deviceRedisService.setDeviceOnline(gateway);

        webSocketMessageService.sendBatch(gateway.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                DeviceAuthorityDTO.builder()
                        .controlSource(gateway.getControlSource())
                        .sn(gateway.getDeviceSn())
                        .type(DroneAuthorityEnum.FLIGHT)
                        .build());
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
                .deviceIndex(Optional.ofNullable(dto.getControlSource())
                        .map(ControlSourceEnum::getControlSource).orElse(null))
                .deviceName(dto.getDeviceName())
                .version(dto.getThingVersion())
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .workspaceId(dto.getWorkspaceId())
                .boundStatus(dto.getBoundStatus())
                .domain(Optional.ofNullable(dto.getDomain()).map(DeviceDomainEnum::getDomain).orElse(null))
                .deviceType(Optional.ofNullable(dto.getType()).map(DeviceTypeEnum::getType).orElse(null))
                .subType(Optional.ofNullable(dto.getSubType()).map(DeviceSubTypeEnum::getSubType).orElse(null))
                .loginTime(dto.getLoginTime() != null ?
                        dto.getLoginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .boundTime(dto.getBoundTime() != null ?
                        dto.getBoundTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .childSn(dto.getChildDeviceSn())
                .firmwareVersion(dto.getFirmwareVersion())
                .compatibleStatus(dto.getFirmwareStatus() == null ? null :
                        DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE != dto.getFirmwareStatus())
                .deviceDesc(dto.getDeviceDesc())
                .build();
    }
}