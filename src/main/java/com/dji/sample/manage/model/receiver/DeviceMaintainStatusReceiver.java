package com.dji.sample.manage.model.receiver;

import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
@Data
public class DeviceMaintainStatusReceiver {

    private List<MaintainStatusReceiver> maintainStatusArray;
}
