package com.dji.sample.manage.service;


import com.dji.sample.manage.model.dto.WorkspaceDTO;

import java.util.Optional;

public interface IWorkspaceService {

    /**
     * Query the workspace information based on the primary key id of the database.
     * @param id primary key id
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceById(int id);

    /**
     * Query the information of a workspace based on its workspace id.
     * @param workspaceId
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId);
}
