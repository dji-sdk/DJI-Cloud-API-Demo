package com.dji.sample.media.controller;

import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sample.media.service.IFileService;
import com.dji.sdk.common.HttpResultResponse;
import com.dji.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@RestController
@RequestMapping("${url.media.prefix}${url.media.version}/files")
public class FileController {

    @Autowired
    private IFileService fileService;

    /**
     * Get information about all the media files in this workspace based on the workspace id.
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/files")
    public HttpResultResponse<PaginationData<MediaFileDTO>> getFilesList(@RequestParam(defaultValue = "1") Long page,
                                                                         @RequestParam(name = "page_size", defaultValue = "10") Long pageSize,
                                                                         @PathVariable(name = "workspace_id") String workspaceId) {
        PaginationData<MediaFileDTO> filesList = fileService.getMediaFilesPaginationByWorkspaceId(workspaceId, page, pageSize);
        return HttpResultResponse.success(filesList);
    }

    /**
     * Query the download address of the file according to the media file id,
     * and redirect to this address directly for download.
     * @param workspaceId
     * @param fileId
     * @param response
     */
    @GetMapping("/{workspace_id}/file/{file_id}/url")
    public void getFileUrl(@PathVariable(name = "workspace_id") String workspaceId,
                           @PathVariable(name = "file_id") String fileId, HttpServletResponse response) {

        try {
            URL url = fileService.getObjectUrl(workspaceId, fileId);
            response.sendRedirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
