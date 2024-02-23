package com.dji.sdk.cloudapi.log;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum LogErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    DEVICE_RESTART(324001, "Device restart interrupts log export."),

    EXPORT_TIMEOUT(324012, "Compressing logs timed out. Too many logs selected. Unselect some logs and try again."),

    PULL_FAILED(324013, "Failed to obtain device log list. Try again later."),

    EMPTY_LOG_LIST(324014, "Device log list is empty. Refresh page or restart dock and try again."),

    AIRCRAFT_SHUTDOWN(324015, "Aircraft powered off or not connected. Unable to obtain log list. Make sure aircraft is inside dock. Remotely power on aircraft and try again."),

    INSUFFICIENT_STORAGE_SPACE(324016, "Insufficient dock storage space. Failed to compress logs. Clear space or try again later."),

    NO_LOG(324017, "Failed to compress logs. Unable to obtain logs of selected aircraft. Refresh page or restart dock and try again."),

    COMPRESSION_FAILED(324018, "Failed to compress logs and submit issue report. Try again later or restart dock and try again."),

    UPLOAD_FAILED(324019, "Due to network anomalies at the airport, the log upload has failed. Please retry later."),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    LogErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    public static LogErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
