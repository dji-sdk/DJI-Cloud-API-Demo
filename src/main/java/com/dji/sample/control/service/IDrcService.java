package com.dji.sample.control.service;

import com.dji.sample.control.model.dto.JwtAclDTO;
import com.dji.sample.control.model.param.DrcConnectParam;
import com.dji.sample.control.model.param.DrcModeParam;
import com.dji.sdk.cloudapi.control.DrcModeMqttBroker;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
public interface IDrcService {

    /**
     * Save the drc mode of dock in redis.
     * @param dockSn
     * @param clientId
     */
    void setDrcModeInRedis(String dockSn, String clientId);

    /**
     * Query the client that is controlling the dock.
     * @param dockSn
     * @return clientId
     */
    String getDrcModeInRedis(String dockSn);

    /**
     * Delete the drc mode of dock in redis.
     * @param dockSn
     * @return
     */
    Boolean delDrcModeInRedis(String dockSn);

    /**
     * Provide mqtt options for the control terminal.
     * @param workspaceId
     * @param userId
     * @param username
     * @param param
     * @return
     */
    DrcModeMqttBroker userDrcAuth(String workspaceId, String userId, String username, DrcConnectParam param);

    /**
     * Make the dock enter drc mode. And grant relevant permissions.
     * @param workspaceId
     * @param param
     * @return
     */
    JwtAclDTO deviceDrcEnter(String workspaceId, DrcModeParam param);

    /**
     * Make the dock exit drc mode.
     * @param workspaceId
     * @param param
     */
    void deviceDrcExit(String workspaceId, DrcModeParam param);
}
