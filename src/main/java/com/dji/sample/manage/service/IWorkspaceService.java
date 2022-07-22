package com.dji.sample.manage.service;


import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.dto.WorkspaceDTO;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;

public interface IWorkspaceService {

    /**
     * Query the information of a workspace based on its workspace id.
     * @param workspaceId
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId);

    /**
     * Query the workspace of a workspace based on bind code.
     * @param bindCode
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceNameByBindCode(String bindCode);

    /**
     * Handle the request for obtaining the organization information corresponding to the device binding.
     * Note: If your business does not need to bind the dock to the organization,
     *       you can directly reply to the successful message without implementing business logic.
     * @param receiver
     */
    void replyOrganizationGet(CommonTopicReceiver receiver, MessageHeaders headers);
}
