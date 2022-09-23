package com.dji.sample.manage.model.param;

import com.dji.sample.manage.model.receiver.LogsFileUpload;
import lombok.Data;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
@Data
public class DeviceLogsCreateParam {

    private String logsInformation;

    private Long happenTime;

    private List<LogsFileUpload> files;
}
