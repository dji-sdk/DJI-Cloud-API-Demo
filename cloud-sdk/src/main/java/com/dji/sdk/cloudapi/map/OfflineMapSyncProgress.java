package com.dji.sdk.cloudapi.map;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapSyncProgress {

    /**
     * Sync status
     */
    private OfflineMapSyncStatusEnum status;

    /**
     * Result code
     */
    private OfflineMapSyncReasonEnum reason;

    /**
     * Offline map file information
     */
    private OfflineMapSyncFile file;

    public OfflineMapSyncProgress() {
    }

    @Override
    public String toString() {
        return "OfflineMapSyncProgress{" +
                "status=" + status +
                ", reason=" + reason +
                ", file=" + file +
                '}';
    }

    public OfflineMapSyncStatusEnum getStatus() {
        return status;
    }

    public OfflineMapSyncProgress setStatus(OfflineMapSyncStatusEnum status) {
        this.status = status;
        return this;
    }

    public OfflineMapSyncReasonEnum getReason() {
        return reason;
    }

    public OfflineMapSyncProgress setReason(OfflineMapSyncReasonEnum reason) {
        this.reason = reason;
        return this;
    }

    public OfflineMapSyncFile getFile() {
        return file;
    }

    public OfflineMapSyncProgress setFile(OfflineMapSyncFile file) {
        this.file = file;
        return this;
    }
}
