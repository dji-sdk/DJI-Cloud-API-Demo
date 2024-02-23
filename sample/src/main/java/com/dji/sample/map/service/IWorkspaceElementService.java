package com.dji.sample.map.service;

import com.dji.sample.map.model.dto.GroupElementDTO;
import com.dji.sdk.cloudapi.map.*;
import com.dji.sdk.common.HttpResultResponse;

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
    List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

    /**
     * Save all the elements, including the information of the elements in the group,
     * and the coordinate information in the elements.
     *
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @param notify
     * @return
     */
    HttpResultResponse saveElement(String workspaceId, String groupId, CreateMapElementRequest elementCreate, boolean notify);

    /**
     * Update the element information based on the element id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     *
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @param username
     * @param notify
     * @return
     */
    HttpResultResponse updateElement(String workspaceId, String elementId, UpdateMapElementRequest elementUpdate, String username, boolean notify);

    /**
     * Delete the element information based on the element id,
     * including the information of the elements in the group, and the coordinate information in the elements.
     *
     * @param workspaceId
     * @param elementId
     * @param notify
     * @return
     */
    HttpResultResponse deleteElement(String workspaceId, String elementId, boolean notify);

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
     *
     * @param workspaceId
     * @param groupId
     * @return
     */
    HttpResultResponse deleteAllElementByGroupId(String workspaceId, String groupId);

    MapElementCreateWsResponse element2CreateWsElement(GroupElementDTO element);

    MapElementUpdateWsResponse element2UpdateWsElement(GroupElementDTO element);
}