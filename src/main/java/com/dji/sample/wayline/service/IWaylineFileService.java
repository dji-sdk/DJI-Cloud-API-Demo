package com.dji.sample.wayline.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.wayline.model.dto.WaylineFileDTO;
import com.dji.sample.wayline.model.param.WaylineQueryParam;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    Optional<WaylineFileDTO> getWaylineByWaylineId(String workspaceId, String waylineId);

    /**
     * Get the download address of the file object.
     * @param workspaceId
     * @param waylineId
     * @return
     */
    URL getObjectUrl(String workspaceId, String waylineId) throws SQLException;

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

    /**
     * Delete the wayline file based on the wayline id.
     * @param workspaceId
     * @param waylineId
     */
    Boolean deleteByWaylineId(String workspaceId, String waylineId);
}
