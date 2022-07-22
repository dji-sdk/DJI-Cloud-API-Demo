package com.dji.sample.wayline.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.PaginationData;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.model.dto.WaylineFileUploadDTO;
import com.dji.sample.wayline.model.param.WaylineQueryParam;
import com.dji.sample.wayline.service.IWaylineFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@RestController
@RequestMapping("${url.wayline.prefix}${url.wayline.version}/workspaces")
public class WaylineFileController {

    @Autowired
    private IWaylineFileService waylineFileService;

    /**
     * Query the basic data of the wayline file according to the query conditions.
     * The query condition field in pilot is fixed.
     * @param orderBy   Sorted fields. Spliced at the end of the sql statement.
     * @param favorited Whether the wayline file is favorited or not.
     * @param page
     * @param pageSize
     * @param templateType
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/waylines")
    public ResponseResult<PaginationData<WaylineFileDTO>> getWaylinesPagination(@RequestParam(name = "order_by") String orderBy,
                                      @RequestParam(required = false) boolean favorited, @RequestParam Integer page,
                                      @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                      @RequestParam(name = "template_type", required = false) Integer[] templateType,
                                      @PathVariable(name = "workspace_id") String workspaceId) {
        WaylineQueryParam param = WaylineQueryParam.builder()
                .favorited(favorited)
                .page(page)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .templateType(templateType)
                .build();
        PaginationData<WaylineFileDTO> data = waylineFileService.getWaylinesByParam(workspaceId, param);
        return ResponseResult.success(data);
    }

    /**
     * Query the download address of the file according to the wayline file id,
     * and redirect to this address directly for download.
     * @param workspaceId
     * @param waylineId
     * @param response
     */
    @GetMapping("/{workspace_id}/waylines/{wayline_id}/url")
    public void getFileUrl(@PathVariable(name = "workspace_id") String workspaceId,
                                @PathVariable(name = "wayline_id") String waylineId, HttpServletResponse response) {

        try {
            URL url = waylineFileService.getObjectUrl(workspaceId, waylineId);
            response.sendRedirect(url.toString());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * When the wayline file is uploaded to the storage server by pilot,
     * the basic information of the file is reported through this interface.
     * @param request
     * @param workspaceId
     * @param uploadFile
     * @return
     */
    @PostMapping("/{workspace_id}/upload-callback")
    public ResponseResult uploadCallBack(HttpServletRequest request,
                                         @PathVariable(name = "workspace_id") String workspaceId,
                                         @RequestBody WaylineFileUploadDTO uploadFile) {

        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);

        WaylineFileDTO metadata = uploadFile.getMetadata();
        metadata.setUsername(customClaim.getUsername());
        metadata.setObjectKey(uploadFile.getObjectKey());
        metadata.setName(uploadFile.getName());

        int id = waylineFileService.saveWaylineFile(workspaceId, metadata);

        return id <= 0 ? ResponseResult.error() : ResponseResult.success();
    }

    /**
     * Favorite the wayline file according to the wayline file id.
     * @param workspaceId
     * @param ids   wayline file id
     * @return
     */
    @PostMapping("/{workspace_id}/favorites")
    public ResponseResult markFavorite(@PathVariable(name = "workspace_id") String workspaceId,
                             @RequestParam(name = "id") List<String> ids) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, true);

        return isMark ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * Delete the favorites of this wayline file based on the wayline file id.
     * @param workspaceId
     * @param ids wayline file id
     * @return
     */
    @DeleteMapping("/{workspace_id}/favorites")
    public ResponseResult unmarkFavorite(@PathVariable(name = "workspace_id") String workspaceId,
                             @RequestParam(name = "id") List<String> ids) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, false);

        return isMark ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * Checking whether the name already exists according to the wayline name must ensure the uniqueness of the wayline name.
     * This interface will be called when uploading waylines and must be available.
     * @param workspaceId
     * @param names
     * @return
     */
    @GetMapping("/{workspace_id}/waylines/duplicate-names")
    public ResponseResult checkDuplicateNames(@PathVariable(name = "workspace_id") String workspaceId,
                                              @RequestParam(name = "name") List<String> names) {
        List<String> existNamesList = waylineFileService.getDuplicateNames(workspaceId, names);

        return ResponseResult.success(existNamesList);
    }

    /**
     * Delete the wayline file in the workspace according to the wayline id.
     * @param workspaceId
     * @param waylineId
     * @return
     */
    @DeleteMapping("/{workspace_id}/waylines/{wayline_id}")
    public ResponseResult deleteWayline(@PathVariable(name = "workspace_id") String workspaceId,
                                        @PathVariable(name = "wayline_id") String waylineId) {
        boolean isDel = waylineFileService.deleteByWaylineId(workspaceId, waylineId);
        return isDel ? ResponseResult.success() : ResponseResult.error("Failed to delete wayline.");
    }
}
