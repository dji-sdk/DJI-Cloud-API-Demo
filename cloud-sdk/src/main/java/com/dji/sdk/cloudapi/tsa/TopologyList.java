package com.dji.sdk.cloudapi.tsa;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Schema(description = "device topology list")
public class TopologyList {

    @Schema(description = "drone device topology collection")
    @NotNull
    private List<@Valid DeviceTopology> hosts;

    @Schema(description = "gateway device topology collection")
    @NotNull
    private List<@Valid DeviceTopology> parents;

    public TopologyList() {
    }

    @Override
    public String toString() {
        return "TopologyList{" +
                "hosts=" + hosts +
                ", parents=" + parents +
                '}';
    }

    public List<DeviceTopology> getHosts() {
        return hosts;
    }

    public TopologyList setHosts(List<DeviceTopology> hosts) {
        this.hosts = hosts;
        return this;
    }

    public List<DeviceTopology> getParents() {
        return parents;
    }

    public TopologyList setParents(List<DeviceTopology> parents) {
        this.parents = parents;
        return this;
    }
}
