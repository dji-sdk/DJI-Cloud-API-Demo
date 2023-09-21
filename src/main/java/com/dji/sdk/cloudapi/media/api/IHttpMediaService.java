package com.dji.sdk.cloudapi.media.api;

import com.dji.sdk.cloudapi.media.*;
import com.dji.sdk.common.HttpResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Tag(name = "media interface")
public interface IHttpMediaService {

    String PREFIX = "media/api/v1";

    /**
     * Check if the file has been uploaded by the fingerprint.
     * @param workspaceId
     * @param request
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "media fast upload", description = "Check if the file has been uploaded by the fingerprint.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/fast-upload")
    HttpResultResponse mediaFastUpload(
            @PathVariable(name = "workspace_id") String workspaceId,
            @Valid @RequestBody MediaFastUploadRequest request,
            HttpServletRequest req, HttpServletResponse rsp);


    /**
     * When the file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param workspaceId
     * @param request
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "app reports file upload result", description = "When the file is uploaded to the storage server by pilot, " +
            "the basic information of the file is reported through this interface.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            }, responses = @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "responseObjectKey",
                            summary = "response object key",
                            description = "response object key",
                            value = "{\"code\": 0, \"message\":\"success\", \"data\": \"media/DJI_20220831151616_0004_W_Waypoint4.JPG\"}"
                    )})))

    @PostMapping(PREFIX + "/workspaces/{workspace_id}/upload-callback")
    HttpResultResponse<String> mediaUploadCallback(
            @PathVariable(name = "workspace_id") String workspaceId,
            @Valid @RequestBody MediaUploadCallbackRequest request,
                HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Query the files that already exist in this workspace based on the workspace id and the collection of tiny fingerprints.
     * @param workspaceId
     * @param request  There is only one tiny_fingerprint parameter in the body.
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "checks whether the file fingerprint exists", description = "Query the files that already exist in this " +
            "workspace based on the workspace id and the collection of tiny fingerprints.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/files/tiny-fingerprints")
    HttpResultResponse<GetFileFingerprintResponse> getExistFileTinyFingerprint(
            @PathVariable(name = "workspace_id") String workspaceId,
            @Valid @RequestBody GetFileFingerprintRequest request,
                HttpServletRequest req, HttpServletResponse rsp);

    /**
     * Report the upload status of the media files in the file group in real time.
     * @param workspaceId
     * @param request
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "callback after the file group upload complete", description = "Report the upload status of " +
            "the media files in the file group in real time.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/group-upload-callback")
    HttpResultResponse folderUploadCallback(
            @PathVariable(name = "workspace_id") String workspaceId,
            @Valid @RequestBody FolderUploadCallbackRequest request,
                HttpServletRequest req, HttpServletResponse rsp);


}
