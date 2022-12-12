package com.dji.sample.wayline.service;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.param.CreateJobParam;
import org.springframework.messaging.MessageHeaders;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public interface IWaylineJobService {

    /**
     * Create wayline job in the database.
     * @param param
     * @param customClaim   user info
     * @return
     */
    Optional<WaylineJobDTO> createWaylineJob(CreateJobParam param, CustomClaim customClaim);

    /**
     * Issue wayline mission to the dock.
     * @param param
     * @param customClaim   user info
     * @return
     */
    ResponseResult publishFlightTask(CreateJobParam param, CustomClaim customClaim) throws SQLException;

    /**
     * Execute the task immediately.
     * @param jobId
     * @throws SQLException
     * @return
     */
    Boolean executeFlightTask(String jobId);

    /**
     * Cancel the task Base on job Ids.
     * @param workspaceId
     * @param jobIds
     * @throws SQLException
     */
    void cancelFlightTask(String workspaceId, Collection<String> jobIds);

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

    /**
     * Process to get interface data of flight mission resources.
     * @param receiver
     * @param headers
     */
    void flightTaskResourceGet(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * Set the media files for this job to upload immediately.
     * @param workspaceId
     * @param jobId
     */
    void uploadMediaHighestPriority(String workspaceId, String jobId);
}
