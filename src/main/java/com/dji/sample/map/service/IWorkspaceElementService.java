package com.dji.sample.map.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.map.model.dto.ElementCreateDTO;
import com.dji.sample.map.model.dto.ElementUpdateDTO;
import com.dji.sample.map.model.dto.GroupDTO;
import com.dji.sample.map.model.dto.GroupElementDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
public interface IWorkspaceElementService {

    /**
     * Query all groups in the workspace based on the workspace's id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @return
     */
    List<GroupDTO> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

    /**
     * Save all the elements, including the information of the elements in the group,
     * and the coordinate information in the elements.
     * @param groupId
     * @param elementCreate
     * @return
     */
    ResponseResult saveElement(String groupId, ElementCreateDTO elementCreate);

    /**
     * Update the element information based on the element id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     * @param elementId
     * @param elementUpdate
     * @param username
     * @return
     */
    ResponseResult updateElement(String elementId, ElementUpdateDTO elementUpdate, String username);

    /**
     * Delete the element information based on the element id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     * @param elementId
     * @return
     */
    ResponseResult deleteElement(String elementId);

    /**
     * Query an element based on the element id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     * @param elementId
     * @return
     */
    Optional<GroupElementDTO> getElementByElementId(String elementId);

    /**
     * Delete all the elements information based on the group id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     * @param groupId
     * @return
     */
    ResponseResult deleteAllElementByGroupId(String groupId);
}