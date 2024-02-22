package com.dji.sample.control.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

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
