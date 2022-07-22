package com.dji.sample.wayline.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.param.CreateJobParam;
import com.dji.sample.wayline.service.IWaylineJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@RequestMapping("${url.wayline.prefix}${url.wayline.version}/workspaces")
@RestController
public class WaylineJobController {

    @Autowired
    private IWaylineJobService waylineJobService;

    /**
     * Create a wayline task for the Dock.
     * @param request
     * @param param
     * @param workspaceId
     * @return
     * @throws SQLException
     */
    @PostMapping("/{workspace_id}/flight-tasks")
    public ResponseResult createJob(HttpServletRequest request, @RequestBody CreateJobParam param,
                                    @PathVariable(name = "workspace_id") String workspaceId) throws SQLException {
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        customClaim.setWorkspaceId(workspaceId);
        boolean isCreate = waylineJobService.createJob(param, customClaim);
        return isCreate ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * Paginate through all jobs in this workspace.
     * @param page
     * @param pageSize
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/jobs")
    public ResponseResult<PaginationData<WaylineJobDTO>> getJobs(@RequestParam(defaultValue = "1") Long page,
                     @RequestParam(name = "page_size", defaultValue = "10") Long pageSize,
                     @PathVariable(name = "workspace_id") String workspaceId) {
        PaginationData<WaylineJobDTO> data = waylineJobService.getJobsByWorkspaceId(workspaceId, page, pageSize);
        return ResponseResult.success(data);
    }

    /**
     * Issue wayline mission to the dock for execution.
     * @param jobId
     * @param workspaceId
     * @return
     * @throws SQLException
     */
    @PostMapping("/{workspace_id}/jobs/{job_id}")
    public ResponseResult publishJob(@PathVariable(name = "job_id") String jobId,
                                     @PathVariable(name = "workspace_id") String workspaceId) throws SQLException {
        waylineJobService.publishFlightTask(workspaceId, jobId);
        return ResponseResult.success();
    }
}
