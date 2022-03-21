package com.dji.sample.manage.service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/2/21
 */
public interface ITSAService {

    /**
     * Real-time push telemetry data.
     * @param workspaceId
     * @param osdData
     * @param sn
     */
    void pushTelemetryData(String workspaceId, Object osdData, String sn);
}
