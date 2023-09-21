package com.dji.sample.control.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Data
public class RemoteDebugParam {

    @NotNull
    private Integer action;

}
