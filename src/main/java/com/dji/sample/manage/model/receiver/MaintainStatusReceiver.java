package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/3
 */
@Data
public class MaintainStatusReceiver {

    private Integer state;

    private Integer lastMaintainType;

    private Long lastMaintainTime;

    private Long lastMaintainWorkSorties;
}
