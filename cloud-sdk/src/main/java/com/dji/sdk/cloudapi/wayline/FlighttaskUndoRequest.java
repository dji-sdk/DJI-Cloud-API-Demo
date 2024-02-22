package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/7
 */
public class FlighttaskUndoRequest extends BaseModel {

    @NotNull
    @Size(min = 1)
    private List<@Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$") String> flightIds;

    public FlighttaskUndoRequest() {
    }

    @Override
    public String toString() {
        return "FlighttaskUndoRequest{" +
                "flightIds=" + flightIds +
                '}';
    }

    public List<String> getFlightIds() {
        return flightIds;
    }

    public FlighttaskUndoRequest setFlightIds(List<String> flightIds) {
        this.flightIds = flightIds;
        return this;
    }
}
