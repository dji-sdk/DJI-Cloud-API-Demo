package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.map.dao.IFlightAreaFileMapper;
import com.dji.sample.map.model.dto.FlightAreaDTO;
import com.dji.sample.map.model.dto.FlightAreaFileDTO;
import com.dji.sample.map.model.entity.FlightAreaFileEntity;
import com.dji.sample.map.service.IFlightAreaFileService;
import com.dji.sample.map.service.IFlightAreaPropertyServices;
import com.dji.sample.map.service.IGroupService;
import com.dji.sample.map.service.IWorkspaceElementService;
import com.dji.sdk.cloudapi.flightarea.*;
import com.dji.sdk.cloudapi.map.ElementCircleGeometry;
import com.dji.sdk.cloudapi.map.ElementPointGeometry;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Service
@Transactional
public class FlightAreaFileServiceImpl implements IFlightAreaFileService {

    @Autowired
    private IFlightAreaFileMapper mapper;

    @Autowired
    private IWorkspaceElementService workspaceElementService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private OssServiceContext ossServiceContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IFlightAreaPropertyServices flightAreaPropertyServices;

    @Override
    public Optional<FlightAreaFileDTO> getFlightAreaFileByFileId(String fileId) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.lambdaQuery(FlightAreaFileEntity.class)
                        .eq(FlightAreaFileEntity::getFileId, fileId)))
                .map(this::entity2Dto);
    }

    @Override
    public Integer saveFlightAreaFile(FlightAreaFileDTO file) {
        FlightAreaFileEntity entity = dto2Entity(file);
        int id = mapper.insert(entity);
        return id > 0 ? entity.getId() : id;
    }

    @Override
    public Integer setNonLatestByWorkspaceId(String workspaceId) {
        return mapper.update(FlightAreaFileEntity.builder().latest(false).build(),
                Wrappers.lambdaUpdate(FlightAreaFileEntity.class)
                        .eq(FlightAreaFileEntity::getWorkspaceId, workspaceId)
                        .eq(FlightAreaFileEntity::getLatest, true));
    }

    @Override
    public Optional<FlightAreaFileDTO> getLatestByWorkspaceId(String workspaceId) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.lambdaQuery(FlightAreaFileEntity.class)
                        .eq(FlightAreaFileEntity::getWorkspaceId, workspaceId)
                        .eq(FlightAreaFileEntity::getLatest, true)
                        .orderByDesc(FlightAreaFileEntity::getUpdateTime)
                        .last(" limit 1")))
                .map(this::entity2Dto);
    }

    @Override
    public FlightAreaFileDTO packageFlightAreaFile(String workspaceId, List<FlightAreaDTO> flightAreas) {
        Optional<FlightAreaFileDTO> fileOpt = getLatestByWorkspaceId(workspaceId);
        if (fileOpt.isPresent()) {
            return fileOpt.get();
        }
        FlightAreaFileDTO file = generateFlightAreaFile(workspaceId, flightAreas);
        int id = saveFlightAreaFile(file);
        if (id <= 0) {
            throw new RuntimeException("Failed to save the flight area file.");
        }
        return file;
    }

    private FlightAreaFileDTO generateFlightAreaFile(String workspaceId, List<FlightAreaDTO> flightAreas) {

        FlightAreaJson flightAreaJson = new FlightAreaJson()
                .setFeatures(flightAreas.stream()
                        .map(this::generateFlightAreaFeature)
                        .collect(Collectors.toList()));
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(64);
             JsonGenerator generator = objectMapper.createGenerator(os);) {
            generator.writePOJO(flightAreaJson);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                String name = String.format("geofence_%s.json", org.springframework.util.DigestUtils.md5DigestAsHex(is));
                is.reset();
                String objectKey = OssConfiguration.objectDirPrefix + "/" + name;
                ossServiceContext.putObject(OssConfiguration.bucket, objectKey, is);
                return FlightAreaFileDTO.builder()
                        .name(name)
                        .objectKey(objectKey)
                        .fileId(UUID.randomUUID().toString())
                        .size(os.size())
                        .workspaceId(workspaceId)
                        .sign(DigestUtils.sha256Hex(os.toByteArray()))
                        .latest(true)
                        .build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private FlightAreaFeature generateFlightAreaFeature(FlightAreaDTO area) {
        GeometrySubTypeEnum subType = null;
        Float radius = 0f;
        if (area.getContent().getGeometry() instanceof ElementCircleGeometry) {
            ElementCircleGeometry geometry = (ElementCircleGeometry) area.getContent().getGeometry();
            subType = GeometrySubTypeEnum.CIRCLE;
            radius = geometry.getRadius();
            area.getContent().setGeometry(new ElementPointGeometry().setCoordinates(geometry.getCoordinates()));
        }
        return new FlightAreaFeature()
                .setGeofenceType(area.getType())
                .setId(area.getAreaId())
                .setProperties(new FeatureProperty()
                        .setSubType(subType)
                        .setRadius(radius)
                        .setEnable(area.getStatus()))
                .setGeometry(objectMapper.convertValue(area.getContent().getGeometry(), FlightAreaGeometry.class));
    }

    private FlightAreaFileDTO entity2Dto(FlightAreaFileEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return FlightAreaFileDTO.builder()
                .fileId(entity.getFileId())
                .name(entity.getName())
                .objectKey(entity.getObjectKey())
                .sign(entity.getSign())
                .size(entity.getSize())
                .workspaceId(entity.getWorkspaceId())
                .latest(entity.getLatest())
                .build();
    }

    private FlightAreaFileEntity dto2Entity(FlightAreaFileDTO dto) {
        if (dto == null) {
            return null;
        }
        return FlightAreaFileEntity.builder()
                .fileId(dto.getFileId())
                .size(dto.getSize())
                .name(dto.getName())
                .sign(dto.getSign())
                .objectKey(dto.getObjectKey())
                .workspaceId(dto.getWorkspaceId())
                .latest(dto.getLatest())
                .build();
    }
}
