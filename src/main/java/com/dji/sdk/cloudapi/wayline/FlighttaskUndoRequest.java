package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/7
 */
public class FlighttaskUndoRequest extends BaseModel {

    @NotNull
    @Size(min = 1)
    private List<@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$") String> flightIds;

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
