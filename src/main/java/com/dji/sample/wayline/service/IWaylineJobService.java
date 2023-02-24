package com.dji.sample.wayline.service;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.enums.WaylineJobStatusEnum;
import com.dji.sample.wayline.model.param.CreateJobParam;
import org.springframework.messaging.MessageHeaders;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
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
     * @param workspaceId   user info
     * @param username      user info
     * @param beginTime     The time the job started.
     * @param endTime       The time the job ended.
     * @return
     */
    Optional<WaylineJobDTO> createWaylineJob(CreateJobParam param, String workspaceId, String username, Long beginTime, Long endTime);

    /**
     * Create a sub-task based on the information of the parent task.
     * @param workspaceId
     * @param parentId
     * @return
     */
    Optional<WaylineJobDTO> createWaylineJobByParent(String workspaceId, String parentId);

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
    Boolean executeFlightTask(String workspaceId, String jobId);

    /**
     * Cancel the task Base on job Ids.
     * @param workspaceId
     * @param jobIds
     * @throws SQLException
     */
    void cancelFlightTask(String workspaceId, Collection<String> jobIds);

    /**
     * Cancel the dock tasks that have been issued but have not yet been executed.
     * @param workspaceId
     * @param dockSn
     * @param jobIds
     */
    void publishCancelTask(String workspaceId, String dockSn, List<String> jobIds);

    /**
     * Query wayline jobs based on conditions.
     * @param workspaceId
     * @param jobIds
     * @param status
     * @return
     */
    List<WaylineJobDTO> getJobsByConditions(String workspaceId, Collection<String> jobIds, WaylineJobStatusEnum status);

    /**
     * Query job information based on job id.
     * @param workspaceId
     * @param jobId
     * @return job information
     */
    Optional<WaylineJobDTO> getJobByJobId(String workspaceId, String jobId);

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
