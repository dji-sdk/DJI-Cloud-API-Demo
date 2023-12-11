package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public class FlighttaskExecuteRequest extends BaseModel {

    /**
     * fix: flightId是应用层提供的,应用层有自己的id构建规则,如果不是设备原因必须指定格式,最好就不要控制flightId的格式了 by witcom@2023.09.22
     */
    @NotNull
    //@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    private String flightId;

    public FlighttaskExecuteRequest() {
    }

    @Override
    public String toString() {
        return "FlighttaskExecuteRequest{" +
                "flightId='" + flightId + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public FlighttaskExecuteRequest setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
