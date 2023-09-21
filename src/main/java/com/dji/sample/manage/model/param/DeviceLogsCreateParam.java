package com.dji.sample.manage.model.param;

import com.dji.sdk.cloudapi.log.FileUploadStartFile;
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

    private List<FileUploadStartFile> files;
}
