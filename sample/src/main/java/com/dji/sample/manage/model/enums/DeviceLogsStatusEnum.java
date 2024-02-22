package com.dji.sample.manage.model.enums;

import com.dji.sdk.cloudapi.log.FileUploadStatusEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
@Getter
public enum DeviceLogsStatusEnum {

    UPLOADING(1, FileUploadStatusEnum.FILE_PULL, FileUploadStatusEnum.FILE_ZIP,
            FileUploadStatusEnum.FILE_UPLOADING, FileUploadStatusEnum.IN_PROGRESS, FileUploadStatusEnum.PAUSED),

    DONE(2, FileUploadStatusEnum.OK),

    CANCELED(3, FileUploadStatusEnum.CANCELED),

    FAILED(4, FileUploadStatusEnum.FAILED, FileUploadStatusEnum.REJECTED, FileUploadStatusEnum.TIMEOUT),

    UNKNOWN(-1);

    int val;

    HashSet<FileUploadStatusEnum> status;

    DeviceLogsStatusEnum(int val, FileUploadStatusEnum... status) {
        this.status = new HashSet<>();
        Collections.addAll(this.status, status);
        this.val = val;
    }

    public static DeviceLogsStatusEnum find(FileUploadStatusEnum status) {
        return Arrays.stream(DeviceLogsStatusEnum.values()).filter(element -> element.status.contains(status)).findAny().orElse(UNKNOWN);
    }
}
