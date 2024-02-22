package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.log.FileUploadProgressExt;
import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class OutputLogsProgressReceiver {

    private FileUploadProgressExt ext;

    private String status;
}
