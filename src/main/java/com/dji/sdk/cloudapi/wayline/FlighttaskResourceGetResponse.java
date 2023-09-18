package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskResourceGetResponse extends BaseModel {

    /**
     * Wayline file object
     */
    @NotNull
    @Valid
    private FlighttaskFile file;

    public FlighttaskResourceGetResponse() {}

    @Override
    public String toString() {
        return "FlighttaskResourceGetResponse{" +
                "file=" + file +
                '}';
    }

    public FlighttaskFile getFile() {
        return file;
    }

    public FlighttaskResourceGetResponse setFile(FlighttaskFile file) {
        this.file = file;
        return this;
    }
}