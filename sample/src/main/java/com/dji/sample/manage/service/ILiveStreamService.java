package com.dji.sample.manage.service;

import com.dji.sample.manage.model.dto.CapacityDeviceDTO;
import com.dji.sample.manage.model.dto.LiveTypeDTO;
import com.dji.sdk.cloudapi.device.VideoId;
import com.dji.sdk.common.HttpResultResponse;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface ILiveStreamService {

    /**
     * Get all the drone data that can be broadcast live in this workspace.
     * @param workspaceId
     * @return
     */
    List<CapacityDeviceDTO> getLiveCapacity(String workspaceId);

    /**
     * Initiate a live streaming by publishing mqtt message.
     * @param liveParam Parameters needed for on-demand.
     * @return
     */
    HttpResultResponse liveStart(LiveTypeDTO liveParam);

    /**
     * Stop the live streaming by publishing mqtt message.
     * @param videoId
     * @return
     */
    HttpResultResponse liveStop(VideoId videoId);

    /**
     * Readjust the clarity of the live streaming by publishing mqtt messages.
     * @param liveParam
     * @return
     */
    HttpResultResponse liveSetQuality(LiveTypeDTO liveParam);

    /**
     * Switches the lens of the device during the live streaming.
     * @param liveParam
     * @return
     */
    HttpResultResponse liveLensChange(LiveTypeDTO liveParam);
}
