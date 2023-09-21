package com.dji.sdk.cloudapi.map.api;

import com.dji.sdk.cloudapi.map.CreateMapElementRequest;
import com.dji.sdk.cloudapi.map.CreateMapElementResponse;
import com.dji.sdk.cloudapi.map.GetMapElementsResponse;
import com.dji.sdk.cloudapi.map.UpdateMapElementRequest;
import com.dji.sdk.common.HttpResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Tag(name = "map interface")
public interface IHttpMapService {

    String PREFIX = "map/api/v1";

    /**
     * In the first connection, pilot will send out this http request to get the group element list.
     * Also, if pilot receives a group refresh instruction from WebSocket,
     * it needs the same interface to request the group element list.
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "get map elements", description = "In the first connection, pilot will send out this http " +
            "request to get the group element list. Also, if pilot receives a group refresh instruction from " +
            "WebSocket, it needs the same interface to request the group element list.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
                    @Parameter(name = "group_id", description = "element group id. The same element group can contain " +
                            "multiple map elements, which is equivalent to grouping map elements. " +
                            "When initiating the request, if the group id parameter is not included, " +
                            "the server needs to return all map elements. If the group id is specified, " +
                            "it only needs to return the set of elements in the specified element group", schema = @Schema(format = "uuid")),
                    @Parameter(name = "is_distributed", description = "Whether the element group is distributed.")
            })
    @GetMapping(PREFIX + "/workspaces/{workspace_id}/element-groups")
    HttpResultResponse<List<GetMapElementsResponse>> getMapElements(
            @PathVariable(name = "workspace_id") String workspaceId,
            @RequestParam(name = "group_id", required = false) String groupId,
            @RequestParam(name = "is_distributed", required = false) Boolean isDistributed,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * When user draws a point, line or polygon on the PILOT/Web side.
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "create map element", description = "When user draws a point, line or polygon on the PILOT/Web side.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
                    @Parameter(name = "group_id", description = "element group id. The same element group can contain " +
                            "multiple map elements, which is equivalent to grouping map elements. " +
                            "When initiating the request, if the group id parameter is not included, " +
                            "the server needs to return all map elements. If the group id is specified, " +
                            "it only needs to return the set of elements in the specified element group", schema = @Schema(format = "uuid"))
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/element-groups/{group_id}/elements")
    HttpResultResponse<CreateMapElementResponse> createMapElement(
            @PathVariable(name = "workspace_id") String workspaceId,
            @PathVariable(name = "group_id") String groupId,
            @Valid @RequestBody CreateMapElementRequest elementCreate,
            HttpServletRequest req, HttpServletResponse rsp);


    /**
     * When user edits a point, line or polygon on the PILOT/Web side.
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "update map element", description = "When user edits a point, line or polygon on the PILOT/Web side.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
                    @Parameter(name = "element_id", description = "element id", schema = @Schema(format = "uuid"))
            })
    @PutMapping(PREFIX + "/workspaces/{workspace_id}/elements/{element_id}")
    HttpResultResponse updateMapElement(
            @PathVariable(name = "workspace_id") String workspaceId,
            @PathVariable(name = "element_id") String elementId,
            @Valid @RequestBody UpdateMapElementRequest elementUpdate,
            HttpServletRequest req, HttpServletResponse rsp);


    /**
     * When user delete a point, line or polygon on the PILOT/Web side.
     * @param workspaceId
     * @param elementId
     * @return
     */
    @Operation(summary = "delete map element", description = "When user delete a point, line or polygon on the PILOT/Web side.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
                    @Parameter(name = "element_id", description = "element id", schema = @Schema(format = "uuid"))
            })
    @DeleteMapping(PREFIX + "/workspaces/{workspace_id}/elements/{element_id}")
    HttpResultResponse deleteMapElement(
            @PathVariable(name = "workspace_id") String workspaceId,
            @PathVariable(name = "element_id") String elementId,
            HttpServletRequest req, HttpServletResponse rsp);

}