package com.dji.sample.control.model.param;

import com.dji.sample.control.model.enums.PayloadCommandsEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
@Data
public class PayloadCommandsParam {

    private String sn;

    @NotNull
    @Valid
    private PayloadCommandsEnum cmd;

    @Valid
    @NotNull
    private DronePayloadParam data;

}
