package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.map.dao.IGroupElementMapper;
import com.dji.sample.map.model.dto.GroupElementDTO;
import com.dji.sample.map.model.entity.GroupElementEntity;
import com.dji.sdk.cloudapi.map.ElementTypeEnum;
import com.dji.sample.map.service.IElementCoordinateService;
import com.dji.sample.map.service.IGroupElementService;
import com.dji.sdk.cloudapi.map.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Service
@Transactional
public class GroupElementServiceImpl implements IGroupElementService {

    @Autowired
    private IGroupElementMapper mapper;

    @Autowired
    private IElementCoordinateService elementCoordinateService;

    @Override
    public List<MapGroupElement> getElementsByGroupId(String groupId) {
        List<GroupElementEntity> elementList = mapper.selectList(
                new LambdaQueryWrapper<GroupElementEntity>()
                        .eq(GroupElementEntity::getGroupId, groupId));

        List<MapGroupElement> groupElementList = new ArrayList<>();
        for (GroupElementEntity elementEntity : elementList) {
            MapGroupElement groupElement = this.entityConvertToDto(elementEntity);
            groupElementList.add(groupElement);

            this.addCoordinateToElement(groupElement, elementEntity);
        }
        return groupElementList;
    }

    @Override
    public Boolean saveElement(String groupId, CreateMapElementRequest elementCreate) {
        Optional<GroupElementEntity> groupElementOpt = this.getEntityByElementId(elementCreate.getId());

        if (groupElementOpt.isPresent()) {
            return false;
        }
        GroupElementEntity groupElement = this.createDtoConvertToEntity(elementCreate);
        groupElement.setGroupId(groupId);

        boolean saveElement = mapper.insert(groupElement) > 0;
        if (!saveElement) {
            return false;
        }
        // save coordinate
        return elementCoordinateService.saveCoordinate(
                elementCreate.getResource().getContent().getGeometry().convertToList(), elementCreate.getId());
    }

    @Override
    public Boolean updateElement(String elementId, UpdateMapElementRequest elementUpdate, String username) {
        Optional<GroupElementEntity> groupElementOpt = this.getEntityByElementId(elementId);
        if (groupElementOpt.isEmpty()) {
            return false;
        }

        GroupElementEntity groupElement = groupElementOpt.get();
        groupElement.setUsername(username);
        this.updateEntityWithDto(elementUpdate, groupElement);
        boolean update = mapper.updateById(groupElement) > 0;
        if (!update) {
            return false;
        }
        // delete all coordinates according to element id.
        boolean delCoordinate = elementCoordinateService.deleteCoordinateByElementId(elementId);
        // save coordinate
        boolean saveCoordinate = elementCoordinateService.saveCoordinate(
                elementUpdate.getContent().getGeometry().convertToList(), elementId);
        return delCoordinate & saveCoordinate;
    }

    @Override
    public Boolean deleteElement(String elementId) {
        Optional<GroupElementEntity> groupElementOpt = this.getEntityByElementId(elementId);
        if (groupElementOpt.isEmpty()) {
            return true;
        }

        GroupElementEntity groupElement = groupElementOpt.get();
        return mapper.deleteById(groupElement.getId()) > 0;
    }


    @Override
    public Optional<GroupElementDTO> getElementByElementId(String elementId) {
        Optional<GroupElementEntity> elementEntityOpt = this.getEntityByElementId(elementId);
        if (elementEntityOpt.isEmpty()) {
            return Optional.empty();
        }
        GroupElementEntity elementEntity = elementEntityOpt.get();
        MapGroupElement groupElement = this.entityConvertToDto(elementEntity);

        this.addCoordinateToElement(groupElement, elementEntity);
        return Optional.ofNullable(groupElement2Dto(groupElement, elementEntity.getGroupId()));
    }

    private GroupElementDTO groupElement2Dto(MapGroupElement element, String groupId) {
        if (null == element) {
            return null;
        }
        return GroupElementDTO.builder()
                .elementId(element.getId())
                .groupId(groupId)
                .updateTime(element.getUpdateTime())
                .createTime(element.getCreateTime())
                .name(element.getName())
                .resource(element.getResource())
                .build();
    }

    /**
     * Adds the received coordinate data to the element object.
     * @param element
     * @param elementEntity
     */
    private void addCoordinateToElement(MapGroupElement element, GroupElementEntity elementEntity) {
        Optional<ElementGeometryType> coordinateOpt = ElementTypeEnum.findType(elementEntity.getElementType());
        if (coordinateOpt.isEmpty()) {
            return;
        }
        element.getResource()
                .setContent(new ElementContent()
                        .setProperties(new ElementProperty()
                                .setClampToGround(elementEntity.getClampToGround())
                                .setColor(elementEntity.getColor()))
                        .setGeometry(coordinateOpt.get()));

        coordinateOpt.get().adapterCoordinateType(
                elementCoordinateService.getCoordinateByElementId(elementEntity.getElementId()));
    }

    /**
     * Query an element based on the element id。
     * @param elementId
     * @return
     */
    private Optional<GroupElementEntity> getEntityByElementId(String elementId) {
        return Optional.ofNullable(mapper.selectOne(
                new LambdaQueryWrapper<GroupElementEntity>()
                        .eq(GroupElementEntity::getElementId, elementId)));
    }

    /**
     * Convert database entity objects into element data transfer object.
     * @param entity
     * @return
     */
    private MapGroupElement entityConvertToDto(GroupElementEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MapGroupElement()
                .setId(entity.getElementId())
                .setName(entity.getElementName())
                .setCreateTime(entity.getCreateTime())
                .setUpdateTime(entity.getUpdateTime())
                .setResource(new ElementResource()
                        .setType(ElementResourceTypeEnum.find(entity.getElementType()))
                        .setUsername(entity.getUsername()));
    }

    /**
     * Convert the received element object into a database entity object.
     * @param elementCreate
     * @return
     */
    private GroupElementEntity createDtoConvertToEntity(CreateMapElementRequest elementCreate) {
        ElementProperty properties = elementCreate.getResource().getContent().getProperties();
        return GroupElementEntity.builder()
                .elementId(elementCreate.getId())
                .elementName(elementCreate.getName())
                .username(elementCreate.getResource().getUsername())
                .elementType(ElementTypeEnum.findVal(elementCreate.getResource().getContent().getGeometry().getType()))
                .clampToGround(properties.getClampToGround() != null && properties.getClampToGround())
                .color(properties.getColor())
                .build();
    }

    /**
     * Add the content that needs to be updated to the entity object to be updated.
     * @param elementUpdate
     * @param groupElement
     */
    private void updateEntityWithDto(UpdateMapElementRequest elementUpdate, GroupElementEntity groupElement) {
        if (elementUpdate == null || groupElement == null) {
            return;
        }

        groupElement.setElementName(elementUpdate.getName());
        groupElement.setElementType(ElementTypeEnum.findVal(elementUpdate.getContent().getGeometry().getType()));
        groupElement.setColor(elementUpdate.getContent().getProperties().getColor());

        Boolean clampToGround = elementUpdate.getContent().getProperties().getClampToGround();
        groupElement.setClampToGround(clampToGround);
    }
}
