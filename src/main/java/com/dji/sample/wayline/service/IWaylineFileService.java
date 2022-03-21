package com.dji.sample.wayline.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.wayline.model.WaylineFileDTO;
import com.dji.sample.wayline.model.WaylineQueryParam;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
public interface IWaylineFileService {

    /**
     * Perform paging queries based on query parameters.
     * @param workspaceId
     * @param param
     * @return
     */
    PaginationData<WaylineFileDTO> getWaylinesByParam(String workspaceId, WaylineQueryParam param);

    /**
     * Query the information of this wayline file according to the wayline file id.
     * @param workspaceId
     * @param waylineId
     * @return
     */
    WaylineFileDTO getWaylineByWaylineId(String workspaceId, String waylineId);

    /**
     * Get the download address of the file object.
     * @param bucket    bucket name
     * @param objectKey object name
     * @return
     */
    URL getObjectUrl(String bucket, String objectKey);

    /**
     * Save the basic information of the wayline file.
     * @param workspaceId
     * @param metadata
     * @return
     */
    Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata);

    /**
     * Updates whether the file is collected or not based on the passed parameters.
     * @param workspaceId
     * @param ids          wayline id
     * @param isFavorite   Whether the wayline file is favorited or not.
     * @return
     */
    Boolean markFavorite(String workspaceId, List<String> ids, Boolean isFavorite);

    /**
     * Batch query for duplicate file names in workspace.
     * @param workspaceId
     * @param names
     * @return
     */
    List<String> getDuplicateNames(String workspaceId, List<String> names);
}
