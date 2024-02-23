package com.dji.sample.map.service;

import com.dji.sdk.cloudapi.map.GetMapElementsResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
public interface IGroupService {

    /**
     * Query all groups in the workspace based on the workspace's id.
     * If the group id does not exist, do not add this filter condition.
     * @param workspaceId
     * @param groupId
     * @param isDistributed Used to define if the group needs to be distributed. Default is true.
     * @return
     */
    List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

    /**
     * Get the custom flight area group under this workspace.
     * @param workspaceId
     * @return
     */
    Optional<GetMapElementsResponse> getCustomGroupByWorkspaceId(String workspaceId);
}
