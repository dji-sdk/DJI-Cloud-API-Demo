package com.dji.sample.wayline.service;

import com.dji.sample.component.mqtt.model.EventsReceiver;
import com.dji.sample.wayline.model.dto.WaylineJobDTO;
import com.dji.sample.wayline.model.dto.WaylineJobKey;
import com.dji.sample.wayline.model.dto.WaylineTaskProgressReceiver;

import java.util.Optional;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/24
 */
public interface IWaylineRedisService {

    /**
     * Save the status of the wayline job performed by the dock into redis.
     * @param dockSn
     * @param data
     */
    void setRunningWaylineJob(String dockSn, EventsReceiver<WaylineTaskProgressReceiver> data);

    /**
     * Query the status of wayline job performed by the dock in redis.
     * @param dockSn
     * @return
     */
    Optional<EventsReceiver<WaylineTaskProgressReceiver>> getRunningWaylineJob(String dockSn);

    /**
     * Delete the wayline job status of the dock operation in redis.
     * @param dockSn
     * @return
     */
    Boolean delRunningWaylineJob(String dockSn);

    /**
     * Save the wayline job suspended by the dock to redis.
     * @param dockSn
     * @param jobId
     */
    void setPausedWaylineJob(String dockSn, String jobId);

    /**
     * Query the wayline job id suspended by the dock in redis.
     * @param dockSn
     * @return
     */
    String getPausedWaylineJobId(String dockSn);

    /**
     * Delete the wayline job suspended by the dock in redis.
     * @param dockSn
     * @return
     */
    Boolean delPausedWaylineJob(String dockSn);

    /**
     * Save the wayline job blocked by the dock to redis.
     * @param dockSn
     * @param jobId
     */
    void setBlockedWaylineJob(String dockSn, String jobId);

    /**
     * Query the wayline job id blocked by the dock in redis.
     * @param dockSn
     * @return
     */
    String getBlockedWaylineJobId(String dockSn);

    /**
     * Delete the wayline job id blocked by the dock in redis.
     * @param dockSn
     * @return
     */
    Boolean delBlockedWaylineJobId(String dockSn);

    /**
     * Save the conditional wayline job by the dock to redis.
     * @param waylineJob
     */
    void setConditionalWaylineJob(WaylineJobDTO waylineJob);

    /**
     * Query the conditional wayline job id by the dock in redis.
     * @param jobId
     * @return
     */
    Optional<WaylineJobDTO> getConditionalWaylineJob(String jobId);

    /**
     * Delete the conditional wayline job by the dock in redis.
     * @param jobId
     * @return
     */
    Boolean delConditionalWaylineJob(String jobId);

    /**
     * Add the wayline job that needs to be issued.
     * @param waylineJob
     * @return
     */
    Boolean addPreparedWaylineJob(WaylineJobDTO waylineJob);

    /**
     * Get the latest wayline job that needs to be issued.
     * @return
     */
    Optional<WaylineJobKey> getNearestPreparedWaylineJob();

    /**
     * Get the time when the wayline job is issued.
     * @param jobKey
     * @return
     */
    Double getPreparedWaylineJobTime(WaylineJobKey jobKey);

    /**
     * Delete the wayline job that needs to be issued in redis.
     * @param jobKey
     * @return
     */
    Boolean removePreparedWaylineJob(WaylineJobKey jobKey);
}
