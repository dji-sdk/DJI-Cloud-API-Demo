package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dji.sample.map.dao.IFlightAreaPropertyMapper;
import com.dji.sample.map.model.dto.FlightAreaPropertyDTO;
import com.dji.sample.map.model.dto.FlightAreaPropertyUpdate;
import com.dji.sample.map.model.entity.FlightAreaPropertyEntity;
import com.dji.sample.map.service.IFlightAreaPropertyServices;
import com.dji.sdk.cloudapi.flightarea.GeofenceTypeEnum;
import com.dji.sdk.cloudapi.flightarea.GeometrySubTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Service
@Transactional
public class FlightAreaPropertyServiceImpl implements IFlightAreaPropertyServices {

    @Autowired
    private IFlightAreaPropertyMapper mapper;

    @Override
    public List<FlightAreaPropertyDTO> getPropertyByElementIds(List<String> elementIds) {
        if (CollectionUtils.isEmpty(elementIds)) {
            return Collections.emptyList();
        }
        return mapper.selectList(
                Wrappers.lambdaQuery(FlightAreaPropertyEntity.class)
                        .in(FlightAreaPropertyEntity::getElementId, elementIds)).stream()
                .map(this::fillProperty).collect(Collectors.toList());
    }

    @Override
    public Integer saveProperty(FlightAreaPropertyDTO property) {
        FlightAreaPropertyEntity entity = dto2Entity(property);
        int id = mapper.insert(entity);
        return id > 0 ? entity.getId() : id;
    }

    @Override
    public Integer deleteProperty(String elementId) {
        return mapper.delete(Wrappers.lambdaUpdate(FlightAreaPropertyEntity.class).eq(FlightAreaPropertyEntity::getElementId, elementId));
    }

    @Override
    public Integer updateProperty(FlightAreaPropertyUpdate property) {
        return mapper.update(update2Entity(property),
                Wrappers.lambdaUpdate(FlightAreaPropertyEntity.class).eq(FlightAreaPropertyEntity::getElementId, property.getElementId()));
    }

    private FlightAreaPropertyDTO fillProperty(FlightAreaPropertyEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        FlightAreaPropertyDTO.FlightAreaPropertyDTOBuilder builder = FlightAreaPropertyDTO.builder()
                .elementId(entity.getElementId())
                .status(entity.getEnable())
                .type(GeofenceTypeEnum.find(entity.getType()))
                .subType(Optional.ofNullable(entity.getSubType()).map(GeometrySubTypeEnum::find).orElse(null))
                .radius(entity.getRadius().floatValue() / 100);

        return builder.build();
    }

    private FlightAreaPropertyEntity dto2Entity(FlightAreaPropertyDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return FlightAreaPropertyEntity.builder()
                .elementId(dto.getElementId())
                .enable(dto.getStatus())
                .subType(Optional.ofNullable(dto.getSubType()).map(GeometrySubTypeEnum::getSubType).orElse(null))
                .type(dto.getType().getType())
                .radius(Optional.ofNullable(dto.getRadius()).map(radius -> radius * 100).map(Float::intValue).orElse(null))
                .build();
    }

    private FlightAreaPropertyEntity update2Entity(FlightAreaPropertyUpdate property) {
        if (Objects.isNull(property)) {
            return null;
        }
        return FlightAreaPropertyEntity.builder()
                .radius(Optional.ofNullable(property.getRadius()).map(radius -> radius * 100).map(Float::intValue).orElse(null))
                .enable(property.getStatus())
                .elementId(property.getElementId())
                .build();
    }
}
