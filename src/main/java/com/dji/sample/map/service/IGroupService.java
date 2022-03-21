package com.dji.sample.map.service;

import com.dji.sample.map.model.dto.GroupDTO;

import java.util.List;

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
    List<GroupDTO> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

}
