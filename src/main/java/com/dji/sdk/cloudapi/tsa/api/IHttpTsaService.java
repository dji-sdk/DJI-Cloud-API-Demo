package com.dji.sdk.cloudapi.tsa.api;

import com.dji.sdk.cloudapi.tsa.TopologyResponse;
import com.dji.sdk.common.HttpResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Tag(name = "tsa interface")
public interface IHttpTsaService {

    String PREFIX = "manage/api/v1";

    /**
     * Get the topology list of all devices in the current user workspace for pilot display.
     * @param workspaceId
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "obtain device topology list", description = "Get the topology list of all devices in the current user workspace for pilot display." +
            "In the first connection, DJI Pilot 2 will call this interface to obtain the list topology of all devices." +
            "Also, when Pilot receives a websocket command to notify the device of online, offline, and update, " +
            "it will also call this interface to request the device topology list to be updated.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
            })
    @GetMapping(PREFIX + "/workspaces/{workspace_id}/devices/topologies")
    HttpResultResponse<TopologyResponse> obtainDeviceTopologyList(
            @PathVariable(name = "workspace_id") String workspaceId,
            HttpServletRequest req, HttpServletResponse rsp);

}
