package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public enum ExecutionStepEnum {

    INITIAL(0, "Initial state"),

    PRE_CHECK(1, "Pre-launch check: Is the spacecraft executing the route?"),

    CHECK_WORK_MODE(2, "Pre-launch check: Is the airport exiting work mode?"),

    CHECK_EXECUTION(3, "Pre-launch check: Route execution in progress"),

    CHECK_RETURN(4, "Pre-launch check: Return in progress"),

    PREPARATION(5, "Route execution entering preparation state, waiting for task issuance to begin"),

    OPERATIONAL(6, "Airport entering operational state"),

    OPEN_COVER_PREPARATION(7, "Entering startup check preparation and hatch opening preparation"),

    WAITING_FOR_FLIGHT_SYSTEM_READINESS(8, "Waiting for flight system readiness, push connection establishment"),

    WAITING_FOR_RTK(9, "Waiting for RTK source monitoring with reported values"),

    CHECK_RTK_SOURCE(10, "Check if RTK source is from the airport; if not, reset"),

    WAITING_FOR_FLIGHT_CONTROL(11, "Waiting for flight control notification"),

    WRESTING_FLIGHT_CONTROL(12, "Airport has no control; wresting control from the aircraft"),

    GET_KMZ(13, "Get the latest KMZ URL"),

    DOWNLOAD_KMZ(14, "Download KMZ"),

    KMZ_UPLOADING(15, "KMZ uploading"),

    DYE_CONFIGURATION(16, "Dye configuration"),

    SET_DRONE_PARAMETER(17, "Aircraft takeoff parameter settings, alternate landing point settings, takeoff altitude settings, dye settings"),

    SET_TAKEOFF_PARAMETER(18, "Aircraft 'flyto' takeoff parameter settings"),

    SET_HOME_POINT(19, "Home point settings"),

    WAYLINE_EXECUTION(20, "Trigger route execution"),

    IN_PROGRESS(21, "Route execution in progress"),

    RETURN_CHECK_PREPARATION(22, "Entering return check preparation"),

    LADING(23, "Aircraft landing at the airport"),

    CLOSE_COVER(24, "Hatch closure after landing"),

    EXIT_WORK_MODE(25, "Airport exiting work mode"),

    DRONE_ABNORMAL_RECOVERY(26, "Airport abnormal recovery"),

    UPLOADING_FLIGHT_SYSTEM_LOGS(27, "Airport uploading flight system logs"),

    CHECK_RECORDING_STATUS(28, "Camera recording status check"),

    GET_MEDIA_FILES(29, "Get the number of media files"),

    DOCK_ABNORMAL_RECOVERY(30, "Abnormal recovery of airport takeoff hatch opening"),

    NOTIFY_TASK_RESULTS(31, "Notify task results"),

    TASK_COMPLETED(32, "Task execution completed; whether to initiate log retrieval based on configuration file"),

    RETRIEVAL_DRONE_LOG_LIST(33, "Log list retrieval - Aircraft list"),

    RETRIEVAL_DOCK_LOG_LIST(34, "Log list retrieval - Airport list retrieval"),

    UPLOAD_LOG_LIST_RESULTS(35, "Log list retrieval - Upload log list results"),

    RETRIEVAL_DRONE_LOG(36, "Log retrieval - Retrieve aircraft logs"),

    RETRIEVAL_DOCK_LOG(37, "Log retrieval - Retrieve airport logs"),

    COMPRESS_DRONE_LOG(38, "Log retrieval - Compress aircraft logs"),

    COMPRESS_DOCK_LOG(39, "Log retrieval - Compress airport logs"),

    UPLOAD_DRONE_LOG(40, "Log retrieval - Upload aircraft logs"),

    UPLOAD_DOCK_LOG(41, "Log retrieval - Upload airport logs"),

    NOTIFY_LOG_RESULTS(42, "Log retrieval - Notify results"),

    WAITING_FOR_SERVICE_RESPONSE(65533, "Waiting for service response after completion"),

    NO_SPECIFIC_STATUS(65534, "No specific status"),

    UNKNOWN(65535, "UNKNOWN");

    private final int step;

    private final String msg;

    ExecutionStepEnum(int step, String msg) {
        this.step = step;
        this.msg = msg;
    }

    @JsonValue
    public int getStep() {
        return step;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static ExecutionStepEnum find(int step) {
        return Arrays.stream(values()).filter(stepEnum -> stepEnum.step == step).findAny()
                .orElseThrow(() -> new CloudSDKException(ExecutionStepEnum.class, step));
    }
}
