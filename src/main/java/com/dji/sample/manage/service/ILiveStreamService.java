package com.dji.sample.manage.service;

import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.manage.model.dto.CapacityDeviceDTO;
import com.dji.sample.manage.model.dto.LiveTypeDTO;
import com.dji.sample.manage.model.receiver.LiveCapacityReceiver;

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
     * Save live capability data.
     * @param liveCapacityReceiver
     * @param timestamp
     */
    void saveLiveCapacity(LiveCapacityReceiver liveCapacityReceiver, Long timestamp);

    /**
     * Initiate a live streaming by publishing mqtt message.
     * @param liveParam Parameters needed for on-demand.
     * @return
     */
    ResponseResult liveStart(LiveTypeDTO liveParam);

    /**
     * Stop the live streaming by publishing mqtt message.
     * @param videoId
     * @return
     */
    ResponseResult liveStop(String videoId);

    /**
     * Readjust the clarity of the live streaming by publishing mqtt messages.
     * @param liveParam
     * @return
     */
    ResponseResult liveSetQuality(LiveTypeDTO liveParam);
}
