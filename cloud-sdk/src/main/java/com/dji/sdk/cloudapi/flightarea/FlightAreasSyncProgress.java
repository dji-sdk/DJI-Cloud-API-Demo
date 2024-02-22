package com.dji.sdk.cloudapi.flightarea;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreasSyncProgress {

    /**
     * Synchronize state
     */
    private FlightAreaSyncStatusEnum status;

    /**
     * Return Code
     */
    private FlightAreaSyncReasonEnum reason;

    /**
     * Custom flight area file
     */
    private FlightAreaFile file;

    public FlightAreasSyncProgress() {
    }

    @Override
    public String toString() {
        return "FlightAreasSyncProgress{" +
                "status=" + status +
                ", reason=" + reason +
                ", file=" + file +
                '}';
    }

    public FlightAreaSyncStatusEnum getStatus() {
        return status;
    }

    public FlightAreasSyncProgress setStatus(FlightAreaSyncStatusEnum status) {
        this.status = status;
        return this;
    }

    public FlightAreaSyncReasonEnum getReason() {
        return reason;
    }

    public FlightAreasSyncProgress setReason(FlightAreaSyncReasonEnum reason) {
        this.reason = reason;
        return this;
    }

    public FlightAreaFile getFile() {
        return file;
    }

    public FlightAreasSyncProgress setFile(FlightAreaFile file) {
        this.file = file;
        return this;
    }
}
