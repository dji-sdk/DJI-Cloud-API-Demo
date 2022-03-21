package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.component.mqtt.model.CommonTopicResponse;
import com.dji.sample.component.mqtt.model.TopicStateReceiver;
import com.dji.sample.component.mqtt.service.IMessageSenderService;
import com.dji.sample.component.mqtt.service.IMqttTopicService;
import com.dji.sample.component.websocket.config.ConcurrentWebSocketSession;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.model.CustomWebSocketMessage;
import com.dji.sample.component.websocket.model.WebSocketManager;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.manage.dao.IDeviceMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DeviceEntity;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.enums.IconUrlEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.model.receiver.StatusGatewayReceiver;
import com.dji.sample.manage.model.receiver.StatusSubDeviceReceiver;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceService;
import com.dji.sample.manage.service.IWorkspaceService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    @Qualifier("gatewayOSDServiceImpl")
    private AbstractTSAService tsaService;

    @Override
    public Boolean deviceOffline(String gatewaySn) {
        List<DeviceDTO> gatewaysList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(gatewaySn)
                        .build());

        // If no information about this gateway device exists in the database, the drone is considered to be offline.
        if (gatewaysList.isEmpty()) {
            log.debug("The drone is already offline.");
            return true;
        }
        // Handle the drone connected to the gateway device offline.
        return this.subDeviceOffline(gatewaysList.get(0).getChildDeviceSn());
    }

    @Override
    public Boolean subDeviceOffline(String deviceSn) {
        // Cancel drone-related subscriptions.
        this.unsubscribeTopicOffline(deviceSn);

        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(deviceSn)
                        .build());

        // If no information about this drone exists in the database, the drone is considered to be offline.
        if (devicesList.isEmpty()) {
            log.debug("{} is already offline.", deviceSn);
            return true;
        }

        List<String> ids = devicesList.stream()
                .map(DeviceDTO::getDeviceSn)
                .collect(Collectors.toList());

        // Delete all data related to the drone.
        boolean isDel = this.delDeviceByDeviceSns(ids);
        payloadService.deletePayloadsByDeviceSn(ids);

        log.debug("{} offline status: {}.", deviceSn, isDel);
        return isDel;
    }

    @Override
    public Boolean deviceOnline(StatusGatewayReceiver deviceGateway) {
        String deviceSn = deviceGateway.getSubDevices().get(0).getSn();

        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(deviceSn)
                        .build());
        // If the information about this drone exists in the database, the drone is considered to be online.
        if (!devicesList.isEmpty()) {
            log.warn("{} is already online.", deviceSn);
            // Subscribe to topic related to drone and gateway devices.
            this.subscribeTopicOnline(deviceGateway.getSn());
            this.subscribeTopicOnline(deviceSn);
            return true;
        }

        // Delete the gateway device information that was previously bound to the drone.
        this.delDeviceByDeviceSns(
                this.getDevicesByParams(
                        DeviceQueryParam.builder()
                                .childSn(deviceSn)
                                .build())
                        .stream()
                        .map(DeviceDTO::getDeviceSn)
                        .collect(Collectors.toList()));

        DeviceEntity gateway = deviceGatewayConvertToDeviceEntity(deviceGateway);
        gateway.setWorkspaceId(WorkspaceDTO.DEFAULT_WORKSPACE_ID);
        // Set the icon of the gateway device displayed in the pilot's map, required in the TSA module.
        gateway.setUrlNormal(IconUrlEnum.NORMAL_PERSON.getUrl());
        // Set the icon of the gateway device displayed in the pilot's map when it is selected, required in the TSA module.
        gateway.setUrlSelect(IconUrlEnum.SELECT_PERSON.getUrl());

        DeviceEntity subDevice = subDeviceConvertToDeviceEntity(deviceGateway.getSubDevices().get(0));
        subDevice.setWorkspaceId(WorkspaceDTO.DEFAULT_WORKSPACE_ID);
        // Set the icon of the drone device displayed in the pilot's map, required in the TSA module.
        subDevice.setUrlSelect(IconUrlEnum.SELECT_EQUIPMENT.getUrl());
        // Set the icon of the drone device displayed in the pilot's map when it is selected, required in the TSA module.
        subDevice.setUrlNormal(IconUrlEnum.NORMAL_EQUIPMENT.getUrl());

        gateway.setChildSn(subDevice.getDeviceSn());

        boolean isSave = this.saveDevice(gateway) > 0 && this.saveDevice(subDevice) > 0;

        log.debug(subDevice.getDeviceSn() + " online status: {}", isSave);
        if (isSave) {
            // Subscribe to topic related to drone and gateway devices.
            this.subscribeTopicOnline(subDevice.getDeviceSn());
            this.subscribeTopicOnline(gateway.getDeviceSn());

        }
        return isSave;
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
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + OSD_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + STATE_SUF);
        topicService.subscribe(THING_MODEL_PRE + PRODUCT + sn + SERVICES_SUF + _REPLY_SUF);
    }

    @Override
    public void unsubscribeTopicOffline(String sn) {
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + OSD_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + STATE_SUF);
        topicService.unsubscribe(THING_MODEL_PRE + PRODUCT + sn + SERVICES_SUF + _REPLY_SUF);
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
                        .eq(param.getDomain() != null,
                                DeviceEntity::getDomain, param.getDomain())
                        .eq(StringUtils.hasText(param.getWorkspaceId()),
                                DeviceEntity::getWorkspaceId, param.getWorkspaceId())
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
                        .domain(DeviceDomainEnum.SUB_DEVICE.getVal())
                        .build());

        devicesList.forEach(device -> {
            this.spliceDeviceTopo(device);
            device.setWorkspaceId(workspaceId);

        });
        return devicesList;
    }

    @Override
    public void spliceDeviceTopo(DeviceDTO device) {

        // remote controller
        List<DeviceDTO> gatewaysList = getDevicesByParams(
                DeviceQueryParam.builder()
                        .childSn(device.getDeviceSn())
                        .build());

        // payloads
        List<DevicePayloadDTO> payloadsList = payloadService
                .getDevicePayloadEntitiesByDeviceSn(device.getDeviceSn());


        device.setGatewaysList(gatewaysList);
        device.setPayloadsList(payloadsList);

    }

    @Override
    public Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn) {
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
    public void pushDeviceOnlineTopo(Collection<ConcurrentWebSocketSession> sessions, String sn) {

        CustomWebSocketMessage<TopologyDeviceDTO> pilotMessage =
                CustomWebSocketMessage.<TopologyDeviceDTO>builder()
                        .timestamp(System.currentTimeMillis())
                        .bizCode(BizCodeEnum.DEVICE_ONLINE.getCode())
                        .data(new TopologyDeviceDTO())
                        .build();

        this.getDeviceTopoForPilot(sn)
                .ifPresent(pilotMessage::setData);

        sendMessageService.sendBatch(sessions, pilotMessage);
    }

    @Override
    public TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device) {
        TopologyDeviceDTO.TopologyDeviceDTOBuilder builder = TopologyDeviceDTO.builder();

        if (device != null) {
            String domain = String.valueOf(DeviceDomainEnum.getVal(device.getDomain()));
            String subType = String.valueOf(device.getSubType());
            String type = String.valueOf(device.getType());

            builder.sn(device.getDeviceSn())
                    .deviceCallsign(device.getDeviceName())
                    .deviceModel(DeviceModelDTO.builder()
                            .domain(domain)
                            .subType(subType)
                            .type(type)
                            .key(domain + "-" + type + "-" + subType)
                            .build())
                    .iconUrls(device.getIconUrl())
                    .build();
        }
        return builder.build();
    }

    @Override
    public void pushDeviceOnlineTopo(String workspaceId, String deviceSn, String gatewaySn) {

        // All connected accounts on the pilot side of this workspace.
        Collection<ConcurrentWebSocketSession> pilotSessions = WebSocketManager
                .getValueWithWorkspaceAndUserType(
                        workspaceId, UserTypeEnum.PILOT.getVal());

        this.pushDeviceOnlineTopo(pilotSessions, deviceSn);
        this.pushDeviceOnlineTopo(pilotSessions, gatewaySn);
        this.pushDeviceUpdateTopo(pilotSessions, deviceSn);
        this.pushDeviceUpdateTopo(pilotSessions, gatewaySn);
    }

    @Override
    public void pushDeviceOfflineTopo(String workspaceId, String gatewaySn) {
        // All connected accounts on the pilot side of this workspace.
        Collection<ConcurrentWebSocketSession> pilotSessions = WebSocketManager
                .getValueWithWorkspaceAndUserType(
                        workspaceId, UserTypeEnum.PILOT.getVal());


        List<DeviceDTO> gatewaysList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .deviceSn(gatewaySn)
                        .build());

        if (!gatewaysList.isEmpty()) {
            String deviceSn = gatewaysList.get(0).getChildDeviceSn();
            this.pushDeviceOfflineTopo(pilotSessions, deviceSn);
            this.pushDeviceUpdateTopo(pilotSessions, deviceSn);
        }

        this.pushDeviceOfflineTopo(pilotSessions, gatewaySn);
        this.pushDeviceUpdateTopo(pilotSessions, gatewaySn);
    }

    @Override
    public void handleOSD(String topic, byte[] payload) {
        TopicStateReceiver receiver;
        try {
            String from = topic.substring((THING_MODEL_PRE + PRODUCT).length(),
                    topic.indexOf(OSD_SUF));

            List<DeviceDTO> deviceList = this.getDevicesByParams(
                    DeviceQueryParam.builder().deviceSn(from).build());
            if (deviceList.isEmpty()) {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            receiver = mapper.readValue(payload, TopicStateReceiver.class);

            CustomWebSocketMessage wsMessage = CustomWebSocketMessage.builder()
                    .timestamp(System.currentTimeMillis()).build();

            JsonNode hostNode = mapper.readTree(payload).findPath("data");

            String workspaceId = deviceList.get(0).getWorkspaceId();
            Collection<ConcurrentWebSocketSession> webSessions = WebSocketManager
                    .getValueWithWorkspaceAndUserType(
                            workspaceId, UserTypeEnum.WEB.getVal());


            tsaService.handleOSD(receiver, from, workspaceId, hostNode, webSessions, wsMessage);

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
    private Integer saveDevice(DeviceEntity entity) {
        DeviceEntity deviceEntity = mapper.selectOne(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDeviceSn, entity.getDeviceSn()));
        // Update the information directly if the device already exists.
        if (deviceEntity != null) {
            entity.setId(deviceEntity.getId());
            mapper.updateById(entity);
            return deviceEntity.getId();
        }
        return mapper.insert(entity) > 0 ? entity.getId() : 0;
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
                .getOneDictionaryInfoByDomainTypeSubType(gateway.getDomain(),
                        gateway.getType(), gateway.getSubType());

        dictionaryOpt.ifPresent(entity ->
                builder.deviceName(entity.getDeviceName())
                        .deviceDesc(entity.getDeviceDesc()));

        return builder
                .deviceSn(gateway.getSn())
                .domain(gateway.getDomain())
                .subType(gateway.getSubType())
                .deviceType(gateway.getType())
                .version(gateway.getVersion())
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
                .getOneDictionaryInfoByDomainTypeSubType(DeviceDomainEnum.SUB_DEVICE.getVal(),
                        device.getType(), device.getSubType());

        dictionaryOpt.ifPresent(dictionary ->
                builder.deviceName(dictionary.getDeviceName())
                        .deviceDesc(dictionary.getDeviceDesc()));

        return builder
                .deviceSn(device.getSn())
                .deviceType(device.getType())
                .subType(device.getSubType())
                .domain(DeviceDomainEnum.SUB_DEVICE.getVal())
                .version(device.getVersion())
                .deviceIndex(device.getIndex())
                .build();
    }

    /**
     * Convert database entity objects into device data transfer object.
     * @param entity
     * @return
     */
    private DeviceDTO deviceEntityConvertToDTO(DeviceEntity entity) {
        DeviceDTO.DeviceDTOBuilder builder = DeviceDTO.builder();

        if (entity != null) {
            builder.deviceSn(entity.getDeviceSn())
                    .childDeviceSn(entity.getChildSn())
                    .deviceName(entity.getDeviceName())
                    .deviceDesc(entity.getDeviceDesc())
                    .deviceIndex(entity.getDeviceIndex())
                    .workspaceId(entity.getWorkspaceId())
                    .type(entity.getDeviceType())
                    .subType(entity.getSubType())
                    .domain(DeviceDomainEnum.getDesc(entity.getDomain()))
                    .iconUrl(IconUrlDTO.builder()
                            .normalUrl(entity.getUrlNormal())
                            .selectUrl(entity.getUrlSelect())
                            .build())
                    .build();
        }
        return builder.build();
    }

}