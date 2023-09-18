package com.dji.sdk.cloudapi.wayline.api;

import com.dji.sdk.cloudapi.wayline.GetWaylineListRequest;
import com.dji.sdk.cloudapi.wayline.GetWaylineListResponse;
import com.dji.sdk.cloudapi.wayline.WaylineUploadCallbackRequest;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Tag(name = "wayline interface")
public interface IHttpWaylineService {

    String PREFIX = "wayline/api/v1";

    /**
     * Query the basic data of the wayline file according to the query conditions.
     * The query condition field in pilot is fixed.
     * @param workspaceId workspace id
     * @param request   get waylines params
     * @param req
     * @param rsp
     * @return  wayline list
     */
    @Operation(summary = "get wayline list", description = "Query the basic data of the wayline file according to " +
            "the query conditions. The query condition field in pilot is fixed.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            })
    @GetMapping(PREFIX + "/workspaces/{workspace_id}/waylines")
    HttpResultResponse<PaginationData<GetWaylineListResponse>> getWaylineList(
            @Valid @ParameterObject GetWaylineListRequest request,
            @PathVariable(name = "workspace_id") String workspaceId,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Query the download address of the file according to the wayline file id,
     * and redirect to this address directly for download.
     * @param workspaceId   workspace id
     * @param waylineId     wayline file id
     * @param req
     * @param rsp
     */
    @Operation(summary = "get wayline file download address", description = "Query the download address of the file " +
            "according to the wayline file id, and redirect to this address directly for download.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid")),
                @Parameter(name = "wayline_id", description = "wayline id", schema = @Schema(format = "uuid"))
            })
    @GetMapping(PREFIX + "/workspaces/{workspace_id}/waylines/{wayline_id}/url")
    void getWaylineFileDownloadAddress(
            @PathVariable(name = "workspace_id") String workspaceId,
            @PathVariable(name = "wayline_id") String waylineId,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Checking whether the name already exists according to the wayline name must ensure the uniqueness of the wayline name.
     * This interface will be called when uploading waylines and must be available.
     * @param workspaceId workspace id
     * @param names  wayline file name collection
     * @param req
     * @param rsp
     * @return  already existing wayline name
     */
    @Operation(summary = "get duplicated wayline name", description = "Checking whether the name already exists " +
            "according to the wayline name must ensure the uniqueness of the wayline name. " +
            "This interface will be called when uploading waylines and must be available.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", required = true),
                @Parameter(name = "name", description = "wayline file name", required = true)
            })
    @GetMapping(PREFIX + "/workspaces/{workspace_id}/waylines/duplicate-names")
    HttpResultResponse<List<String>> getDuplicatedWaylineName(
            @PathVariable(name = "workspace_id") String workspaceId,
            @NotNull @Size(min = 1) @RequestParam(name = "name") List<String> names,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * When the wayline file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param workspaceId   workspace id
     * @param request   upload callback params
     * @param req
     * @param rsp
     * @return  success
     */
    @Operation(summary = "file upload result report", description = "When the wayline file is uploaded to the " +
            "storage server by pilot, the basic information of the file is reported through this interface.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", required = true)
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/upload-callback")
    HttpResultResponse fileUploadResultReport(
            @PathVariable(name = "workspace_id") String workspaceId,
            @Valid @RequestBody WaylineUploadCallbackRequest request,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Favorite the wayline file according to the wayline file id.
     * @param workspaceId   workspace id
     * @param ids   wayline file id
     * @param req
     * @param rsp
     * @return  success
     */
    @Operation(summary = "batch favorites wayline", description = "Favorite the wayline file according to the wayline file id.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", required = true),
                @Parameter(name = "id", description = "wayline id", required = true)
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/favorites")
    HttpResultResponse batchFavoritesWayline(
            @PathVariable(name = "workspace_id") String workspaceId,
            @NotNull @Size(min = 1) @RequestParam(name = "id") List<String> ids,
            HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Delete the favorites of this wayline file based on the wayline file id.
     * @param workspaceId   workspace id
     * @param ids   wayline file id
     * @param req
     * @param rsp
     * @return  success
     */
    @Operation(summary = "batch unfavorites wayline", description = "Delete the favorites of this wayline file based on the wayline file id.",
            parameters = {
                @Parameter(name = "workspace_id", description = "workspace id", required = true),
                @Parameter(name = "id", description = "wayline id", required = true)
            })
    @DeleteMapping(PREFIX + "/workspaces/{workspace_id}/favorites")
    HttpResultResponse batchUnfavoritesWayline(
            @PathVariable(name = "workspace_id") String workspaceId,
            @NotNull @Size(min = 1) @RequestParam(name = "id") List<String> ids,
            HttpServletRequest req, HttpServletResponse rsp);
}
