package com.dji.sdk.cloudapi.flightarea;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreasGetResponse extends BaseModel {

    /**
     * File list
     */
    @NotNull
    private List<@Valid FlightAreaGetFile> files;

    public FlightAreasGetResponse() {
    }

    @Override
    public String toString() {
        return "FlightAreasGetResponse{" +
                "files=" + files +
                '}';
    }

    public List<FlightAreaGetFile> getFiles() {
        return files;
    }

    public FlightAreasGetResponse setFiles(List<FlightAreaGetFile> files) {
        this.files = files;
        return this;
    }
}
