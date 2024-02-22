package com.dji.sdk.cloudapi.map;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapGetResponse extends BaseModel {

    /**
     * This parameter allows the dock to turn off the offline map capability of the aircraft.
     */
    @NotNull
    private Boolean offlineMapEnable;

    /**
     * Offline map file object list.
     */
    @NotNull
    private List<@Valid OfflineMapFile> files;

    public OfflineMapGetResponse() {
    }

    @Override
    public String toString() {
        return "OfflineMapGetResponse{" +
                "offlineMapEnable=" + offlineMapEnable +
                ", files=" + files +
                '}';
    }

    public Boolean getOfflineMapEnable() {
        return offlineMapEnable;
    }

    public OfflineMapGetResponse setOfflineMapEnable(Boolean offlineMapEnable) {
        this.offlineMapEnable = offlineMapEnable;
        return this;
    }

    public List<OfflineMapFile> getFiles() {
        return files;
    }

    public OfflineMapGetResponse setFiles(List<OfflineMapFile> files) {
        this.files = files;
        return this;
    }
}
