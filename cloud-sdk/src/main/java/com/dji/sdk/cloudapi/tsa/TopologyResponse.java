package com.dji.sdk.cloudapi.tsa;

import com.dji.sdk.common.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/16
 */
@Schema(description = "topology response data")
public class TopologyResponse extends BaseModel {

    @NotNull
    private List<@Valid TopologyList> list;

    public TopologyResponse() {
    }

    @Override
    public String toString() {
        return "TopologyResponse{" +
                "list=" + list +
                '}';
    }

    public List<TopologyList> getList() {
        return list;
    }

    public TopologyResponse setList(List<TopologyList> list) {
        this.list = list;
        return this;
    }
}
