package com.dji.sample.map.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Data
public class SyncFlightAreaParam {

    @NotNull
    @JsonProperty("device_sn")
    private List<String> deviceSns;

}
