package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.DrcStatusErrorEnum;
import com.dji.sample.manage.model.enums.DockDrcStateEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/17
 */
@Data
public class DrcStatusNotifyReceiver {

    private DrcStatusErrorEnum result;

    private DockDrcStateEnum drcState;
}
