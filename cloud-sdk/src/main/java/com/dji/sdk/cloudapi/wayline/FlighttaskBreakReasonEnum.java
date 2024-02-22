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
public enum FlighttaskBreakReasonEnum {

    NORMAL(0, "No abnormalities"),

    NOT_ID(1, "Mission ID does not exist. The wayline mission has not been executed."),

    UNCOMMON_ERROR(2, "Uncommon error, please contact technical support."),

    ERROR_LOADING_FILE(4, "Error loading wayline file when requesting to start/resume the wayline mission, please try uploading the file again or contact technical support."),

    ERROR_BREAKPOINT_FILE(5, "Failed to query breakpoint file when requesting breakpoint information. Failed to parse breakpoint type when requesting to resume the wayline mission."),

    INCORRECT_PARAMETER(6, "Incorrect cmd parameter when requesting to start/end the wayline mission, incorrect protocol command in the request. Failed to parse breakpoint type when requesting to resume the wayline mission."),

    PARSING_FILE_TIMEOUT(7, "Timeout parsing the WPMZ file when requesting to start/resume the wayline mission, please retry."),

    ALREADY_STARTED(257, "Wayline has already started, cannot start again."),

    UNABLE_TO_INTERRUPT_WAYLINE(258, "Unable to interrupt the wayline in this state, only allowed to pause the wayline in the executing state."),

    NOT_STARTED(259, "Wayline has not started, cannot end the wayline."),

    FLIGHT_MISSION_CONFLICT(261, "Flight mission conflict, unable to obtain control of the aircraft, not allowed to start the wayline during landing and return."),

    UNABLE_TO_RESUME_WAYLINE(262, "Unable to resume wayline in this state, only allowed when the wayline is paused."),

    MAXIMUM_ALTITUDE_LIMIT(513, "Aircraft exceeded the maximum altitude limit."),

    MAXIMUM_DISTANCE_LIMIT(514, "Aircraft exceeded the maximum distance limit."),

    TOO_LOW_HEIGHT(516, "The height of the drone is too low."),

    OBSTACLE_AVOIDANCE(517, "Aircraft triggered obstacle sensing."),

    POOR_RTK(518, "Poor RTK signal"),

    BOUNDARY_OF_RESTRICTED_ZONE(519, "Approaching the boundary of Restricted Zone."),

    GEO_ALTITUDE_LIMIT(521, "Exceeded the dock's GEO zone altitude limit."),

    TAKEOFF_REQUEST_FAILED(522, "Failed to request takeoff for the wayline."),

    TAKEOFF_EXECUTION_FAILED(523, "Takeoff mission execution failed."),

    WAYLINE_MISSION_REQUEST_FAILED(524, "Failed to request wayline mission."),

    RTK_FIXING_REQUEST_FAILED(526, "Failed to request wayline RTK fixing mission."),

    RTK_FIXING_EXECUTION_FAILED(527, "Wayline RTK fixing mission failed to run."),

    WEAK_GPS(769, "Weak GPS signal."),

    ERROR_RC_MODE(770, "Remote controller not in N mode, unable to start the task."),

    HOME_POINT_NOT_REFRESHED(771, "Home point not refreshed."),

    LOW_BATTERY(772, "Unable to start the mission due to low current battery level."),

    LOW_BATTERY_RTH(773, "Wayline interrupted due to low battery causing return to home."),

    RC_DISCONNECTION(775, "Disconnection between the remote controller and the aircraft."),

    ON_THE_GROUND(778, "Aircraft is on the ground with propellers spinning, not allowed to start the wayline."),

    ABNORMAL_VISUAL_STATUS(779, "Abnormal visual status (for example, too bright, too dark, inconsistent brightness on both sides) during real-time terrain follow."),

    INVALID_ALTITUDE(780, "Real-time terrain-following altitude set by the user is invalid (greater than 200m or less than 30m)."),

    CALCULATION_ERROR(781, "Global map calculation error during real-time terrain follow."),

    STRONG_WINDS_RTH(784, "Wayline interrupted due to strong winds causing return to home."),

    USER_EXIT(1281, "User exit."),

    USER_INTERRUPTION(1282, "User interruption."),

    USER_TRIGGERED_RTH(1283, "User triggered return to home."),

    INCORRECT_START_INFORMATION(1539, "Incorrect start information (waypoint index or progress)."),

    UNSUPPORTED_COORDINATE_SYSTEM(1540, "Using an unsupported coordinate system."),

    UNSUPPORTED_ALTITUDE_MODE(1541, "Using an unsupported altitude mode."),

    UNSUPPORTED_TRANSITIONAL_WAYLINE_MODE(1542, "Using an unsupported transitional wayline mode."),

    UNSUPPORTED_YAW_MODE(1543, "Using an unsupported yaw mode."),

    UNSUPPORTED_YAW_DIRECTION_REVERSAL_MODE(1544, "Using an unsupported yaw direction reversal mode."),

    UNSUPPORTED_WAYPOINT_TYPE(1545, "Using an unsupported waypoint type."),

    INVALID_COORDINATED_TURNING_TYPE(1546, "Coordinated turning type cannot be used for the start and end points."),

    INVALID_GLOBAL_SPEED(1547, "Wayline global speed exceeds a reasonable range."),

    WAYPOINT_NUMBER_ABNORMAL(1548, "Waypoint number abnormal."),

    INVALID_LATITUDE_AND_LONGITUDE(1549, "Abnormal latitude and longitude data."),

    ABNORMAL_TURNING_INTERCEPT(1550, "Abnormal turning intercept."),

    INVALID_SEGMENT_MAXIMUM_SPEED(1551, "Maximum speed of wayline segment exceeds a reasonable range."),

    INVALID_TARGET_SPEED(1552, "Wayline segment target speed exceeds a reasonable range."),

    INVALID_YAW_ANGLE(1553, "Waypoint yaw angle exceeds a reasonable range."),

    BREAKPOINT_INVALID_MISSION_ID(1555, "Input mission_id of resuming from breakpoint is wrong."),

    BREAKPOINT_INVALID_PROGRESS_INFORMATION(1556, "Progress information of resuming from breakpoint input error."),

    BREAKPOINT_ERROR_MISSION_STATE(1557, "Mission state of resuming from breakpoint is abnormal."),

    BREAKPOINT_INVALID_INDEX_INFORMATION(1558, "Wapoint index information of resuming from breakpoint input error."),

    BREAKPOINT_INCORRECT_LATITUDE_AND_LONGITUDE(1559, "Incorrect latitude and longitude information for resuming from breakpoint."),

    BREAKPOINT_INVALID_YAW(1560, "Yaw input error for waypoints during resuming from breakpoint."),

    BREAKPOINT_INCORRECT_FLAG_SETTING(1561, "Incorrect flag setting for resuming from breakpoint."),

    WAYLINE_GENERATION_FAILED(1563, "Wayline generation failed."),

    WAYLINE_EXECUTION_FAILED(1564, "Wayline execution failed."),

    WAYLINE_OBSTACLE_SENSING(1565, "Emergency stop due to wayline obstacle sensing."),

    UNRECOGNIZED_ACTION_TYPE(1588, "Unrecognized action type."),

    DUPLICATE_ACTION_ID(1595, "Action ID of same action group can not be the same."),

    ACTION_ID_NOT_65535(1598, "Action ID value cannot be 65535."),

    INVALID_NUMBER_OF_ACTION_GROUPS(1602, "Number of action groups exceeds a reasonable range."),

    ERROR_EFFECTIVE_RANGE(1603, "Error in action group effective range."),

    BREAKPOINT_INVALID_ACTION_INDEX(1606, "Action index exceeds a reasonable range during resuming from breakpoint."),

    BREAKPOINT_TRIGGER_RUNNING_ABNORMAL(1608, "Trigger running result of breakpoint information is abnormal."),

    BREAKPOINT_DUPLICATE_ACTION_GROUP_ID(1609, "Action group ID information can not be duplicated during resume from breakpoint."),

    BREAKPOINT_DUPLICATE_ACTION_GROUP_POSITION(1610, "Action group positions cannot be repeated during resuming from breakpoint."),

    BREAKPOINT_INVALID_ACTION_GROUP_POSITION(1611, "Action group positions exceed a reasonable range during resuming from breakpoint."),

    BREAKPOINT_INVALID_ACTION_ID(1612, "Action ID is not in the breakpoint information during resuming."),

    BREAKPOINT_UNABLE_TO_INTERRUPT(1613, "Cannot modify the action state to interrupt during resuming."),

    INCORRECT_BREAKPOINT_INFORMATION(1614, "Resume failure due to incorrect breakpoint information."),

    BREAKPOINT_UNRECOGNIZED_ACTION_TYPE(1634, "Unrecognized action type."),

    BREAKPOINT_UNRECOGNIZED_TRIGGER_TYPE(1649, "Unrecognized trigger type."),

    UNKNOWN_ERROR_1(65534, "Unknown error."),

    UNKNOWN_ERROR_2(65535, "Unknown error."),

    ;

    private final int reason;

    private final String msg;

    FlighttaskBreakReasonEnum(int reason, String msg) {
        this.reason = reason;
        this.msg = msg;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static FlighttaskBreakReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(FlighttaskBreakReasonEnum.class, reason));
    }
}
