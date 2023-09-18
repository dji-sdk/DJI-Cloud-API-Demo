package com.dji.sample.wayline.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.service.IWaylineFileService;
import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.dji.sdk.cloudapi.wayline.*;
import com.dji.sdk.cloudapi.wayline.api.IHttpWaylineService;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@RestController
public class WaylineFileController implements IHttpWaylineService {

    @Autowired
    private IWaylineFileService waylineFileService;

    /**
     * Delete the wayline file in the workspace according to the wayline id.
     * @param workspaceId
     * @param waylineId
     * @return
     */
    @DeleteMapping("${url.wayline.prefix}${url.wayline.version}/workspaces/{workspace_id}/waylines/{wayline_id}")
    public HttpResultResponse deleteWayline(@PathVariable(name = "workspace_id") String workspaceId,
                                            @PathVariable(name = "wayline_id") String waylineId) {
        boolean isDel = waylineFileService.deleteByWaylineId(workspaceId, waylineId);
        return isDel ? HttpResultResponse.success() : HttpResultResponse.error("Failed to delete wayline.");
    }

    /**
     * Import kmz wayline files.
     * @param file
     * @return
     */
    @PostMapping("${url.wayline.prefix}${url.wayline.version}/workspaces/{workspace_id}/waylines/file/upload")
    public HttpResultResponse importKmzFile(HttpServletRequest request, MultipartFile file) {
        if (Objects.isNull(file)) {
            return HttpResultResponse.error("No file received.");
        }
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        String workspaceId = customClaim.getWorkspaceId();
        String creator = customClaim.getUsername();
        waylineFileService.importKmzFile(file, workspaceId, creator);
        return HttpResultResponse.success();
    }

    /**
     * Query the basic data of the wayline file according to the query conditions.
     * The query condition field in pilot is fixed.
     * @param request
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<PaginationData<GetWaylineListResponse>> getWaylineList(@Valid GetWaylineListRequest request, String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        PaginationData<GetWaylineListResponse> data = waylineFileService.getWaylinesByParam(workspaceId, request);
        return HttpResultResponse.success(data);
    }

    /**
     * Query the download address of the file according to the wayline file id,
     * and redirect to this address directly for download.
     * @param workspaceId
     * @param waylineId
     * @param req
     * @param rsp
     */
    @Override
    public void getWaylineFileDownloadAddress(String workspaceId, String waylineId, HttpServletRequest req, HttpServletResponse rsp) {
        try {
            URL url = waylineFileService.getObjectUrl(workspaceId, waylineId);
            rsp.sendRedirect(url.toString());

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking whether the name already exists according to the wayline name must ensure the uniqueness of the wayline name.
     * This interface will be called when uploading waylines and must be available.
     * @param workspaceId
     * @param names
     * @return
     */
    @Override
    public HttpResultResponse<List<String>> getDuplicatedWaylineName(String workspaceId, @NotNull @Size(min = 1) List<String> names, HttpServletRequest req, HttpServletResponse rsp) {
        List<String> existNamesList = waylineFileService.getDuplicateNames(workspaceId, names);

        return HttpResultResponse.success(existNamesList);
    }

    /**
     * When the wayline file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param request
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse fileUploadResultReport(String workspaceId, @Valid WaylineUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim customClaim = (CustomClaim)req.getAttribute(TOKEN_CLAIM);

        WaylineUploadCallbackMetadata metadata = request.getMetadata();

        WaylineFileDTO file = WaylineFileDTO.builder()
                .username(customClaim.getUsername())
                .objectKey(request.getObjectKey())
                .name(request.getName())
                .templateTypes(metadata.getTemplateTypes().stream().map(WaylineTypeEnum::getValue).collect(Collectors.toList()))
                .payloadModelKeys(metadata.getPayloadModelKeys().stream().map(DeviceEnum::getDevice).collect(Collectors.toList()))
                .droneModelKey(metadata.getDroneModelKey().getDevice())
                .build();

        int id = waylineFileService.saveWaylineFile(workspaceId, file);

        return id <= 0 ? HttpResultResponse.error() : HttpResultResponse.success();
    }

    /**
     * Favorite the wayline file according to the wayline file id.
     * @param workspaceId
     * @param ids   wayline file id
     * @return
     */
    @Override
    public HttpResultResponse batchFavoritesWayline(String workspaceId, @NotNull @Size(min = 1) List<String> ids, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, true);

        return isMark ? HttpResultResponse.success() : HttpResultResponse.error();
    }

    /**
     * Delete the favorites of this wayline file based on the wayline file id.
     * @param workspaceId
     * @param ids wayline file id
     * @return
     */
    @Override
    public HttpResultResponse batchUnfavoritesWayline(String workspaceId, @NotNull @Size(min = 1) List<String> ids, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, false);

        return isMark ? HttpResultResponse.success() : HttpResultResponse.error();
    }
}
