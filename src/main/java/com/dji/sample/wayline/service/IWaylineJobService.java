package com.dji.sample.wayline.service;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.param.CreateJobParam;

import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public interface IWaylineJobService {

    /**
     * Create a wayline mission for the dock.
     * @param param
     * @param customClaim user info
     * @return
     */
    Boolean createJob(CreateJobParam param, CustomClaim customClaim) throws SQLException;

    /**
     * Issue wayline mission to the dock for execution.
     * @param workspaceId
     * @param jobId
     * @return
     */
    void publishFlightTask(String workspaceId, String jobId) throws SQLException;

    /**
     * Query job information based on job id.
     * @param jobId
     * @return job information
     */
    Optional<WaylineJobDTO> getJobByJobId(String jobId);

    /**
     * Update job data.
     * @param dto
     * @return
     */
    Boolean updateJob(WaylineJobDTO dto);

    /**
     * Paginate through all jobs in this workspace.
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    PaginationData<WaylineJobDTO> getJobsByWorkspaceId(String workspaceId, long page, long pageSize);
}
