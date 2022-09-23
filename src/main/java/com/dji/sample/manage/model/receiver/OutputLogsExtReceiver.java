package com.dji.sample.manage.model.receiver;

import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class OutputLogsExtReceiver {

    private List<LogsExtFileReceiver> files;
}
