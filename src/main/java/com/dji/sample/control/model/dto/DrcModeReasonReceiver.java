package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.DrcModeReasonEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
@Data
public class DrcModeReasonReceiver {

    private DrcModeReasonEnum reason;
}
