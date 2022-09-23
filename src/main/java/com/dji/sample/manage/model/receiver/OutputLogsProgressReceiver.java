package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class OutputLogsProgressReceiver {

    private OutputLogsExtReceiver ext;

    private String status;
}
