package com.dji.sample.media.controller;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.media.model.MediaFileDTO;
import com.dji.sample.media.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseResult<List<MediaFileDTO>> getFilesList(@PathVariable(name = "workspace_id") String workspaceId) {
        List<MediaFileDTO> filesList = fileService.getAllFilesByWorkspaceId(workspaceId);
        return ResponseResult.success(filesList);
    }
}
