package com.dji.sample.map.service.impl;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.map.model.dto.ElementCreateDTO;
import com.dji.sample.map.model.dto.ElementUpdateDTO;
import com.dji.sample.map.model.dto.GroupDTO;
import com.dji.sample.map.model.dto.GroupElementDTO;
import com.dji.sample.map.service.IElementCoordinateService;
import com.dji.sample.map.service.IGroupElementService;
import com.dji.sample.map.service.IGroupService;
import com.dji.sample.map.service.IWorkspaceElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Transactional
@Service
public class WorkspaceElementServiceImpl implements IWorkspaceElementService {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupElementService groupElementService;

    @Autowired
    private IElementCoordinateService elementCoordinateService;

    @Override
    public List<GroupDTO> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed) {
        List<GroupDTO> groupList = groupService.getAllGroupsByWorkspaceId(workspaceId, groupId, isDistributed);
        groupList.forEach(group -> group.setElements(
                groupElementService.getElementsByGroupId(group.getId())
        ));
        return groupList;
    }

    @Override
    public ResponseResult saveElement(String groupId, ElementCreateDTO elementCreate) {
        boolean saveElement = groupElementService.saveElement(groupId, elementCreate);
        if (!saveElement) {
            return ResponseResult.error("Failed to save the element.");
        }

        // save coordinate
        boolean saveCoordinate = elementCoordinateService.saveCoordinate(
                        elementCreate.getResource().getContent().getGeometry().convertToList(), elementCreate.getId());

        return saveCoordinate ?
                ResponseResult.success() : ResponseResult.error("Failed to save the coordinate.");
    }


    @Override
    public ResponseResult updateElement(String elementId, ElementUpdateDTO elementUpdate, String username) {
        boolean updElement = groupElementService.updateElement(elementId, elementUpdate, username);
        if (!updElement) {
            return ResponseResult.error("Failed to update the element.");
        }

        // delete all coordinates according to element id.
        boolean delCoordinate = elementCoordinateService.deleteCoordinateByElementId(elementId);
        // save coordinate
        boolean saveCoordinate = elementCoordinateService.saveCoordinate(
                elementUpdate.getContent().getGeometry().convertToList(), elementId);

        return delCoordinate && saveCoordinate ?
                ResponseResult.success() : ResponseResult.error("Failed to update the coordinate.");
    }

    @Override
    public ResponseResult deleteElement(String elementId) {
        boolean delElement = groupElementService.deleteElement(elementId);
        if (!delElement) {
            return ResponseResult.error("Failed to delete the element.");
        }

        // delete all coordinates according to element id.
        boolean delCoordinate = elementCoordinateService.deleteCoordinateByElementId(elementId);

        return delCoordinate ?
                ResponseResult.success() : ResponseResult.error("Failed to delete the coordinate.");
    }

    @Override
    public Optional<GroupElementDTO> getElementByElementId(String elementId) {
        return groupElementService.getElementByElementId(elementId);
    }

    @Override
    public ResponseResult deleteAllElementByGroupId(String groupId) {
        List<GroupElementDTO> groupElementList = groupElementService.getElementsByGroupId(groupId);
        for (GroupElementDTO groupElement : groupElementList) {
            ResponseResult response = this.deleteElement(groupElement.getElementId());
            if (ResponseResult.CODE_SUCCESS != response.getCode()) {
                return response;
            }
        }
        return ResponseResult.success();
    }
}
