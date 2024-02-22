package com.dji.sample.map.service.impl;

import com.dji.sample.common.error.CommonErrorEnum;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.TelemetryDTO;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sample.map.model.dto.*;
import com.dji.sample.map.model.enums.FlightAreaGeometryTypeEnum;
import com.dji.sample.map.model.enums.FlightAreaOpertaionEnum;
import com.dji.sample.map.model.param.PostFlightAreaParam;
import com.dji.sample.map.model.param.PutFlightAreaParam;
import com.dji.sample.map.service.*;
import com.dji.sdk.cloudapi.flightarea.*;
import com.dji.sdk.cloudapi.flightarea.api.AbstractFlightAreaService;
import com.dji.sdk.cloudapi.map.*;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.SDKManager;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.events.TopicEventsRequest;
import com.dji.sdk.mqtt.events.TopicEventsResponse;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import com.dji.sdk.mqtt.services.ServicesReplyData;
import com.dji.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Service
@Transactional
@Slf4j
public class FlightAreaServiceImpl extends AbstractFlightAreaService implements IFlightAreaService {

    @Autowired
    private IFlightAreaPropertyServices flightAreaPropertyServices;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IWorkspaceElementService workspaceElementService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private SDKFlightAreaService abstractFlightAreaService;

    @Autowired
    private IFlightAreaFileService flightAreaFileService;

    @Autowired
    private IDeviceFlightAreaService deviceFlightAreaService;

    @Autowired
    private OssServiceContext ossServiceContext;

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<FlightAreaDTO> getFlightAreaByAreaId(String areaId) {
        List<FlightAreaPropertyDTO> properties = flightAreaPropertyServices.getPropertyByElementIds(List.of(areaId));
        if (CollectionUtils.isEmpty(properties)) {
            return Optional.empty();
        }
        Optional<GroupElementDTO> elementOpt = workspaceElementService.getElementByElementId(areaId);
        return elementOpt.map(groupElementDTO -> this.element2FlightArea(objectMapper.convertValue(groupElementDTO, MapGroupElement.class), properties.get(0)));
    }

    @Override
    public List<FlightAreaDTO> getFlightAreaList(String workspaceId) {
        Optional<List<GetMapElementsResponse>> elementsOpt = groupService.getCustomGroupByWorkspaceId(workspaceId)
                .map(group -> workspaceElementService.getAllGroupsByWorkspaceId(workspaceId, group.getId(), null));
        if (elementsOpt.isEmpty()) {
            return Collections.emptyList();
        }
        List<MapGroupElement> elements = elementsOpt.get().get(0).getElements();
        Map<String, FlightAreaPropertyDTO> propertyMap = flightAreaPropertyServices.getPropertyByElementIds(
                elements.stream().map(MapGroupElement::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(FlightAreaPropertyDTO::getElementId, property -> property));
        return elements.stream().map(element -> this.element2FlightArea(element, propertyMap.get(element.getId()))).collect(Collectors.toList());
    }

    @Override
    public void createFlightArea(String workspaceId, String username, PostFlightAreaParam param) {
        Optional<GetMapElementsResponse> groupOpt = groupService.getCustomGroupByWorkspaceId(workspaceId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("The custom flight area group does not exist, please create it first.");
        }
        ElementGeometryType geometry = param.getContent().getGeometry();
        String type = geometry.getType();
        ElementResourceTypeEnum typeEnum;
        FlightAreaPropertyDTO property = new FlightAreaPropertyDTO();
        property.setType(param.getType());
        property.setStatus(true);
        property.setElementId(param.getId());
        if (GeometrySubTypeEnum.CIRCLE.getSubType().equals(type)) {
            ElementCircleGeometry circleGeometry = (ElementCircleGeometry) geometry;
            property.setRadius(circleGeometry.getRadius());
            property.setSubType(GeometrySubTypeEnum.find(type));
            typeEnum = ElementResourceTypeEnum.find(ElementResourceTypeEnum.POINT.getTypeName());
            param.getContent().setGeometry(new ElementPointGeometry().setCoordinates(circleGeometry.getCoordinates()));
        } else {
            typeEnum = ElementResourceTypeEnum.find(type);
        }
        HttpResultResponse response = workspaceElementService.saveElement(workspaceId, groupOpt.get().getId(),
                new CreateMapElementRequest().setId(param.getId()).setName(param.getName())
                        .setResource(new ElementResource()
                                .setUsername(username)
                                .setType(typeEnum)
                                .setContent(new ElementContent()
                                        .setGeometry(param.getContent().getGeometry())
                                        .setProperties(param.getContent().getProperties()))), false);
        if (HttpResultResponse.CODE_FAILED == response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }

        int id = flightAreaPropertyServices.saveProperty(property);
        if (id <= 0) {
            throw new RuntimeException("Failed to save flight area properties.");
        }
        flightAreaFileService.setNonLatestByWorkspaceId(workspaceId);

        webSocketMessageService.sendBatch(workspaceId, BizCodeEnum.FLIGHT_AREAS_UPDATE.getCode(),
                FlightAreaWs.builder()
                        .operation(FlightAreaOpertaionEnum.ADD)
                        .areaId(param.getId())
                        .username(username)
                        .type(param.getType())
                        .name(param.getName())
                        .updateTime(System.currentTimeMillis())
                        .createTime(System.currentTimeMillis())
                        .content(FlightAreaContent.builder().geometry(geometry).properties(param.getContent().getProperties()).build())
                        .status(true)
                        .build());
    }

    @Override
    public void syncFlightArea(String workspaceId, List<String> deviceSns) {
        for (String deviceSn : deviceSns) {
            Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
            if (deviceOpt.isEmpty() || !workspaceId.equals(deviceOpt.get().getWorkspaceId())) {
                throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
            }
            TopicServicesResponse<ServicesReplyData> response = abstractFlightAreaService.flightAreasUpdate(SDKManager.getDeviceSDK(deviceSn));
            if (!response.getData().getResult().isSuccess()) {
                throw new RuntimeException(response.getData().getResult().getMessage());
            }
        }
        packageFlightArea(workspaceId);
    }

    @Override
    public FlightAreaFileDTO packageFlightArea(String workspaceId) {
        List<FlightAreaDTO> flightAreas = getFlightAreaList(workspaceId);
        return flightAreaFileService.packageFlightAreaFile(workspaceId, flightAreas);
    }

    @Override
    public void deleteFlightArea(String workspaceId, String areaId) {
        HttpResultResponse response = workspaceElementService.deleteElement(workspaceId, areaId, false);
        if (HttpResultResponse.CODE_SUCCESS !=response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }
        int id = flightAreaPropertyServices.deleteProperty(areaId);
        if (id <= 0) {
            throw new RuntimeException("Failed to delete the flight area property.");
        }
        flightAreaFileService.setNonLatestByWorkspaceId(workspaceId);
        webSocketMessageService.sendBatch(workspaceId, BizCodeEnum.FLIGHT_AREAS_UPDATE.getCode(),
                FlightAreaWs.builder()
                        .operation(FlightAreaOpertaionEnum.DELETE)
                        .areaId(areaId)
                        .build());
    }

    @Override
    public void updateFlightArea(String workspaceId, String areaId, PutFlightAreaParam param) {
        Float radius = null;
        if (Objects.nonNull(param.getContent())) {
            ElementGeometryType geometry = param.getContent().getGeometry();
            FlightAreaGeometryTypeEnum typeEnum = FlightAreaGeometryTypeEnum.find(geometry.getType());
            if (FlightAreaGeometryTypeEnum.CIRCLE == typeEnum) {
                radius = ((ElementCircleGeometry) geometry).getRadius();
                geometry = new ElementPointGeometry().setCoordinates(((ElementCircleGeometry) geometry).getCoordinates());
            }
            Optional<GroupElementDTO> elementOpt = workspaceElementService.getElementByElementId(areaId);
            if (elementOpt.isEmpty() || !elementOpt.get().getResource().getType().getTypeName().equals(geometry.getType())) {
                throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
            }
            workspaceElementService.updateElement(workspaceId, areaId,
                    new UpdateMapElementRequest()
                            .setName(param.getName())
                            .setContent(new ElementContent()
                                    .setProperties(param.getContent().getProperties())
                                    .setGeometry(geometry)),
                    null, false);
        }

        int id = flightAreaPropertyServices.updateProperty(FlightAreaPropertyUpdate.builder()
                .elementId(areaId).status(param.getStatus()).radius(radius).build());
        if (id <= 0) {
            throw new RuntimeException("Failed to update flight area properties.");
        }
        flightAreaFileService.setNonLatestByWorkspaceId(workspaceId);
        Optional<FlightAreaDTO> areaOpt = getFlightAreaByAreaId(areaId);
        areaOpt.ifPresent(area -> webSocketMessageService.sendBatch(workspaceId,
                BizCodeEnum.FLIGHT_AREAS_UPDATE.getCode(),
                FlightAreaWs.builder()
                        .operation(FlightAreaOpertaionEnum.UPDATE)
                        .areaId(areaId)
                        .name(area.getName())
                        .content(area.getContent())
                        .status(area.getStatus())
                        .type(area.getType())
                        .username(area.getUsername())
                        .createTime(area.getCreateTime())
                        .updateTime(area.getUpdateTime())
                        .build()));

    }

    @Override
    public TopicEventsResponse<MqttReply> flightAreasSyncProgress(TopicEventsRequest<FlightAreasSyncProgress> request, MessageHeaders headers) {
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(request.getGateway());
        if (deviceOpt.isEmpty()) {
            log.warn("method: flight_areas_sync_progress. Dock is offline.");
            return null;
        }

        FlightAreasSyncProgress data = request.getData();
        String workspaceId = deviceOpt.get().getWorkspaceId();

        DeviceFlightAreaDTO deviceFlightArea = DeviceFlightAreaDTO.builder()
                .deviceSn(request.getGateway())
                .workspaceId(workspaceId)
                .syncStatus(data.getStatus())
                .syncCode(data.getReason())
                .build();
        deviceFlightAreaService.updateOrSaveDeviceFile(deviceFlightArea);
        webSocketMessageService.sendBatch(workspaceId, BizCodeEnum.FLIGHT_AREAS_SYNC_PROGRESS.getCode(),
                FlightAreaNotifyDTO.builder()
                        .sn(request.getGateway())
                        .result(data.getReason().getReason())
                        .message(data.getReason().getMsg())
                        .status(data.getStatus().getStatus())
                        .build());
        return new TopicEventsResponse<>();
    }

    @Override
    public TopicEventsResponse<MqttReply> flightAreasDroneLocation(TopicEventsRequest<FlightAreasDroneLocation> request, MessageHeaders headers) {
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(request.getGateway());
        if (deviceOpt.isEmpty()) {
            log.warn("method: flight_areas_drone_location. Dock is offline.");
            return null;
        }
        if (request.getData().getDroneLocations().isEmpty()) {
            return new TopicEventsResponse<>();
        }
        webSocketMessageService.sendBatch(deviceOpt.get().getWorkspaceId(), BizCodeEnum.FLIGHT_AREAS_DRONE_LOCATION.getCode(),
                TelemetryDTO.<FlightAreasDroneLocation>builder().sn(deviceOpt.get().getChildDeviceSn()).host(request.getData()).build());
        return new TopicEventsResponse<>();
    }

    @Override
    public TopicRequestsResponse<MqttReply<FlightAreasGetResponse>> flightAreasGet(TopicRequestsRequest<FlightAreasGetRequest> request, MessageHeaders headers) {
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(request.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }
        DeviceDTO device = deviceOpt.get();
        Optional<DeviceFlightAreaDTO> flightAreaOpt = deviceFlightAreaService.getDeviceFlightAreaByDevice(device.getWorkspaceId(), device.getDeviceSn());
        Optional<FlightAreaFileDTO> fileOpt = Optional.empty();
        if (flightAreaOpt.isPresent()) {
            fileOpt = flightAreaFileService.getFlightAreaFileByFileId(flightAreaOpt.get().getFileId());
        }
        FlightAreaFileDTO file = fileOpt.orElse(null);
        if (flightAreaOpt.isEmpty() || fileOpt.isEmpty()) {
            file = packageFlightArea(device.getWorkspaceId());
        }
        return new TopicRequestsResponse<MqttReply<FlightAreasGetResponse>>().setData(
                MqttReply.success(new FlightAreasGetResponse().setFiles(
                        List.of(new FlightAreaGetFile()
                                .setName(file.getName())
                                .setSize(file.getSize())
                                .setChecksum(file.getSign())
                                .setUrl(ossServiceContext.getObjectUrl(OssConfiguration.bucket, file.getObjectKey()).toString())
                        ))));
    }

    private FlightAreaDTO element2FlightArea(MapGroupElement element, FlightAreaPropertyDTO property) {
        FlightAreaDTO.FlightAreaDTOBuilder builder = FlightAreaDTO.builder()
                .areaId(element.getId())
                .name(element.getName())
                .createTime(element.getCreateTime())
                .updateTime(element.getUpdateTime())
                .username(element.getResource().getUsername())
                .content(FlightAreaContent.builder()
                        .properties(element.getResource().getContent().getProperties())
                        .geometry(element.getResource().getContent().getGeometry())
                        .build());
        if (Objects.isNull(property)) {
            return builder.build();
        }
        FlightAreaDTO flightArea = builder.type(property.getType()).status(property.getStatus()).build();
        if (GeometrySubTypeEnum.CIRCLE == property.getSubType()) {
            ElementPointGeometry point = (ElementPointGeometry) flightArea.getContent().getGeometry();
            flightArea.getContent().setGeometry(new ElementCircleGeometry()
                    .setRadius(property.getRadius())
                    .setCoordinates(new Double[]{point.getCoordinates()[0], point.getCoordinates()[1]}));
        }
        return flightArea;
    }
}
