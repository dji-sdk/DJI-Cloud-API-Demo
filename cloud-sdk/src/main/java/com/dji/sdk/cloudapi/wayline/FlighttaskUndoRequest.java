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

    /**
     * fix: flightId是应用层提供的,应用层有自己的id构建规则,如果不是设备原因必须指定格式,最好就不要控制flightId的格式了 by witcom@2023.09.22
     */
    @NotNull
    @Size(min = 1)
    private List</*@Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")*/ String> flightIds;

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
