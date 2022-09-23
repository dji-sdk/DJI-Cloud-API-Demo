package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.map.dao.IGroupElementMapper;
import com.dji.sample.map.model.dto.*;
import com.dji.sample.map.model.entity.GroupElementEntity;
import com.dji.sample.map.model.enums.ElementTypeEnum;
import com.dji.sample.map.service.IElementCoordinateService;
import com.dji.sample.map.service.IGroupElementService;
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
    public List<GroupElementDTO> getElementsByGroupId(String groupId) {
        List<GroupElementEntity> elementList = mapper.selectList(
                new LambdaQueryWrapper<GroupElementEntity>()
                        .eq(GroupElementEntity::getGroupId, groupId));

        List<GroupElementDTO> groupElementList = new ArrayList<>();
        for (GroupElementEntity elementEntity : elementList) {

            GroupElementDTO groupElement = this.entityConvertToDto(elementEntity);
            groupElementList.add(groupElement);

            this.addCoordinateToElement(groupElement, elementEntity);
        }
        return groupElementList;
    }

    @Override
    public Boolean saveElement(String groupId, ElementCreateDTO elementCreate) {
        Optional<GroupElementEntity> groupElementOpt = this.getEntityByElementId(elementCreate.getId());

        if (groupElementOpt.isPresent()) {
            return false;
        }
        GroupElementEntity groupElement = this.createDtoConvertToEntity(elementCreate);
        groupElement.setGroupId(groupId);

        return mapper.insert(groupElement) > 0;
    }

    @Override
    public Boolean updateElement(String elementId, ElementUpdateDTO elementUpdate, String username) {
        Optional<GroupElementEntity> groupElementOpt = this.getEntityByElementId(elementId);
        if (groupElementOpt.isEmpty()) {
            return false;
        }

        GroupElementEntity groupElement = groupElementOpt.get();
        groupElement.setUsername(username);
        this.updateEntityWithDto(elementUpdate, groupElement);

        return mapper.updateById(groupElement) > 0;
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
        GroupElementDTO groupElement = this.entityConvertToDto(elementEntity);

        this.addCoordinateToElement(groupElement, elementEntity);

        return Optional.ofNullable(groupElement);
    }

    /**
     * Adds the received coordinate data to the element object.
     * @param element
     * @param elementEntity
     */
    private void addCoordinateToElement(GroupElementDTO element, GroupElementEntity elementEntity) {
        Optional<ElementType> coordinateOpt = ElementTypeEnum.findType(elementEntity.getElementType());
        if (coordinateOpt.isEmpty()) {
            return;
        }

        ElementType elementType = coordinateOpt.get();

        element.getResource().setContent(
                ResourceContentDTO.builder()
                        .properties(ContentPropertyDTO.builder()
                                .clampToGround(elementEntity.getClampToGround())
                                .color(elementEntity.getColor())
                                .build())
                        .geometry(elementType)
                        .build());

        elementType.adapterCoordinateType(
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
    private GroupElementDTO entityConvertToDto(GroupElementEntity entity) {
        GroupElementDTO.GroupElementDTOBuilder builder = GroupElementDTO.builder();
        if (entity == null) {
            return builder.build();
        }

        return builder
                .display(entity.getDisplay())
                .groupId(entity.getGroupId())
                .elementId(entity.getElementId())
                .name(entity.getElementName())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .resource(ElementResourceDTO.builder()
                        .type(entity.getElementType())
                        .username(entity.getUsername())
                        .build())
                .build();
    }

    /**
     * Convert the received element object into a database entity object.
     * @param elementCreate
     * @return
     */
    private GroupElementEntity createDtoConvertToEntity(ElementCreateDTO elementCreate) {
        ContentPropertyDTO properties = elementCreate.getResource().getContent().getProperties();
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
    private void updateEntityWithDto(ElementUpdateDTO elementUpdate, GroupElementEntity groupElement) {
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
