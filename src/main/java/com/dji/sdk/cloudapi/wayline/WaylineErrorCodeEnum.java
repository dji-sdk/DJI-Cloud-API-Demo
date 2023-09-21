package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum WaylineErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SUCCESS(0, "success"),

    WRONG_PARAM(314001, "Failed to distribute task. Try again later"),

    MD5_EMPTY(314002, "The issued wayline task md5 is empty."),

    WRONG_WAYLINE_FILE(314003, "Wayline file format not supported. Check file."),

    DISTRIBUTE_TASK_FAILED_1(314004, "Failed to distribute task."),

    MD5_CHECK_FAILED(314005, "Wayline MD5 check failed."),

    INITIATE_AIRCRAFT_FAILED_1(314006, "Failed to initiate aircraft. Restart dock and try again."),

    TRANSFER_KMZ_FILE_FAILED(314007, "Failed to distribute wayline file from dock to aircraft."),

    PREPARATION_TIMED_OUT(314008, "Aircraft task preparation timed out. Restart dock and try again."),

    INITIATE_AIRCRAFT_FAILED_2(314009, "Failed to initiate aircraft. Restart dock and try again."),

    PERFORM_TASK_FAILED(314010, "Unable to perform task."),

    QUERY_TIMEOUT(314011, "Wayline execution result query timed out."),

    PREPARATION_FAILED_1(314012, "Aircraft task preparation failed. Unable to perform task. Restart dock and try again."),

    WRONG_KMZ_URL(314013, "Get KMZ download address failed."),

    DOCK_SYSTEM_ERROR_1(314014, "Dock system error. Failed to perform task. Try again later."),

    CLOSE_FOURTH_GENERATION_FAILED(314015, "Failed to distribute AI-Spot Check wayline from dock to aircraft. Unable to perform task. Try again later or restart dock and try again."),

    PROCESS_KMZ_FILE_FAILED_1(314016, "Failed to process flight route file. Unable to perform task. Check file."),

    MODIFY_KMZ_FILE_FAILED(314017, "Failed to modify the KMZ file of AI Spot-Check."),

    AIRCRAFT_RTK_ERROR(314018, "Aircraft RTK positioning error. Unable to perform task. Try again later or restart dock and try again."),

    CONVERGE_RTK_FAILED_1(314019, "Failed to converge aircraft RTK data. Unable to perform task. Try again later or restart dock and try again."),

    AIRCRAFT_POSITION_ERROR(314020, "Aircraft not in the middle of landing pad or aircraft heading incorrect. Unable to perform task. Check aircraft position and heading."),

    AIRCRAFT_RTK_POSITIONING_ERROR(314021, "Aircraft RTK positioning error. Unable to perform task. Try again later or restart dock and try again."),

    MODIFY_KMZ_BREAKPOINT_FILE_FAILED(314022, "Failed to modify KMZ file of resuming flight from breakpoint"),

    SETTING_BACKUP_LANDING_POINT_FAILED(316001, "Backup landing point setting failed"),

    SETTING_BACKUP_SAFE_HEIGHT_FAILED(316002, "Backup safe height for transfer setting failed"),

    SETTING_TAKEOFF_HEIGHT_FAILED(316003, "Take-off height setting failed. Note: The default safe take-off height of the aircraft is set to 1.8 meters by dock. The aircraft will fly to 1.8 meters after take-off, and cannot be interrupted during the 0-1.8 meters take-off process, and other actions can only be performed after take-off. This altitude is used by the dock by default and does not support modification. The purpose is to prevent personal injury."),

    SETTING_OUT_OF_CONTROL_ACTION_FAILED(316004, "Out of control action setting failed."),

    CONVERGE_RTK_FAILED_2(316005, "Failed to converge aircraft RTK data. Unable to perform task. Restart dock and try again."),

    DOCK_PREPARATION_FAILED(316006, "Aircraft unable to land on dock. Dock cover closed or driving rods pushed into place.  Check aircraft status on dock deployment site."),

    INITIATE_AIRCRAFT_FAILED(316007, "Failed to initiate aircraft. Restart dock and try again."),

    OBTAIN_FLIGHT_CONTROL_FAILED(316008, "Dock failed to obtain aircraft flight control. Unable to perform task. Make sure flight control not locked by remote controller."),

    LOW_POWER(316009, "Aircraft battery level low. Unable to perform task. Wait until aircraft is charged up to 50% and try again"),

    AIRCRAFT_NOT_DETECTED(316010, "Aircraft not detected. Unable to perform task. Check if aircraft is inside dock and linked to dock, or restart dock and try again."),

    LANDED_ON_INCORRECT_LOCATION(316011, "Aircraft landed on incorrect location. Check if aircraft should be manually placed on dock deployment site."),

    FOLDER_COLORING_FAILED(316012, "Aircraft task preparation failed. Folder coloring failed."),

    OBTAIN_BATTERY_POWER_FAILED(316013, "Query of battery power failed."),

    FLIGHT_CONTROL_PUSHING_TIMED_OUT(316014, "The receive of flight control pushing timed out."),

    AIRCRAFT_LOCATION_TOO_FAR(316015, "Aircraft location calibrated by RTK device is far from dock. Unable to perform task. Restart dock and try again."),

    LANDING_TIMEOUT(316016, "Aircraft landing on dock timed out. Aircraft and dock may be disconnected. Check livestream view to see if aircraft landed on dock"),

    OBTAIN_MEDIA_TIMEOUT(316017, "Obtaining number of aircraft media files timed out. Aircraft and dock may be disconnected. Check livestream view to see if aircraft landed on dock"),

    TASK_PERFORMANCE_TIMED_OUT(316018, "Task performance timed out. Aircraft and dock may be disconnected. Check livestream view to see if aircraft landed on dock"),

    CAMERA_COLORING_TIMED_OUT(316019, "Camera coloring timed out"),

    RTK_SOURCE_ERROR(316020, "Aircraft RTK signal source error."),

    RTK_SOURCE_TIMEOUT(316021, "Checking aircraft RTK signal source timed out."),

    AIRCRAFT_NOT_CONNECTED(316022, "Aircraft unable to return to home. Check if aircraft is powered on, aircraft and dock are connected, and try again"),

    NO_FLIGHT_CONTROL_1(316023, "Aircraft controlled by Controller B and unable to return to home. Control aircraft from Controller B or power off remote controller and try again."),

    WRONG_COMMAND(316024, "Aircraft failed to return to home. Check if aircraft has taken off and try again."),

    SETTING_AIRCRAFT_PARAMETERS_FAILED(316025, "Failed to configure aircraft parameters. Try again later or restart dock and try again."),

    EMERGENCY_BUTTON_PRESSED_DOWN(316026, "Dock emergency stop button pressed down. Unable to perform task. Release button and try again."),

    SETTING_AIRCRAFT_PARAMETERS_TIMEOUT(316027, "Setting aircraft parameters timed out. Try again later or restart dock and try again."),

    FLYING_TO_BACKUP_POINT_1(316029, "Dock emergency stop button pressed down. Aircraft flying to alternate landing site. Make sure aircraft has safely landed and place aircraft inside dock"),

    REFRESH_HOME_POINT_FAILED(316030, "Refresh of home point failed. Please try again."),

    SETTING_RTH_MODE_FAILED(316031, "Failed to set return home mode. Please try again."),

    LOW_POWER_LANDING_OUTSIDE(316050, "The aircraft has landed outside the dock due to low battery. Please check immediately whether the aircraft has landed safely and return the aircraft to the dock."),

    TASK_ABNORMAL_LANDING_OUTSIDE(316051, "The wayline task is abnormal, the aircraft landed outside the dock, please check immediately whether the aircraft has landed safely and return the aircraft to the dock."),

    FLYING_TO_BACKUP_POINT_2(316052, "The wayline task is abnormal, the aircraft will fly to the backup landing point, please check immediately whether the aircraft has landed safely and return the aircraft to the dock."),

    USER_CONTROL_LANDING(316053, "The user controls the aircraft to land."),

    OBTAIN_MEDIA_FAILED(317001, "Failed to obtain number of aircraft media files."),

    CAMERA_NOT_CONNECTED(317002, "Failed to format aircraft storage. Make sure aircraft is powered on and connected to dock and camera can be detected. Or restart aircraft and try again."),

    FORMAT_AIRCRAFT_STORAGE_FAILED(317003, "Failed to format aircraft storage."),

    FORMAT_MEDIA_FILES_FAILED(317004, "Failed to format media files."),

    STOP_RECORDING_FAILED(317005, "Aircraft video recording terminated unsuccessfully, media files for this flight mission may not be able to be uploaded."),

    NOT_IDLE(319001, "Unable to perform task. Dock is performing task or uploading issue logs. Wait until task is complete or logs uploaded and try again."),

    DOCK_SYSTEM_ERROR_2(319002, "Dock system error. Restart dock and try again."),

    TASK_ID_NOT_EXIST(319003, "Task ID doesn't exist in dock"),

    TASK_EXPIRE(319004, "The task has expired."),

    FLIGHTTASK_EXECUTE_COMMAND_TIMEOUT(319005, "Execution command delivery timed out. Unable to perform task."),

    CANCEL_TASK_FAILED_1(319006, "Failed to cancel task. Task in progress."),

    EDIT_TASK_FAILED(319007, "Failed to edit task. Task in progress."),

    TIME_NOT_SYNCED(319008, "Dock and cloud time not synced. Dock unable to perform task."),

    DISTRIBUTE_TASK_FAILED_2(319009, "Failed to distribute task. Try again later or restart dock and try again."),

    VERSION_TOO_EARLY(319010, "Dock firmware version too early. Unable to perform task. Update dock to latest version and try again."),

    INITIALIZING_DOCK(319015, "Initializing dock. Unable to perform task. Wait until initialization is complete."),

    PERFORMING_OTHER_TASK(319016, "Dock performing other task. Unable to perform current task."),

    PROCESSING_MEDIA_FILE(319017, "Dock processing media files captured in last task. Unable to perform current task. Try again later."),

    EXPORTING_LOGS(319018, "Unable to perform task. Dock uploading issue logs. Try again later."),

    PULLING_LOGS(319019, "Unable to perform task. Dock obtaining issue logs. Try again later."),

    PAUSE_TASK_FAILED(319020, "Failed to pause flight task."),

    DISABLE_FLIGHT_CONTROL_FAILED(319021, "Failed to disable Live Flight Controls."),

    FLYTO_TASK_FAILED(319022, "FlyTo task failed."),

    STOP_FLYTO_TASK_FAILED(319023, "Failed to stop FlyTo task."),

    TAKING_OFF_TASK_FAILED(319024, "One-key taking off failed."),

    TASK_IN_PREPARATION(319025, "Task in preparation. Dock unable to perform task distributed from cloud. Try again later"),

    LOW_POWER_THAN_SET_VALUE(319026, "Aircraft battery level lower than set value. Unable to perform task. Wait until charging completes and try again."),

    INSUFFICIENT_STORAGE(319027, "Insufficient storage on dock or aircraft. Unable to perform task. Wait until media files are uploaded to cloud and try again."),

    NO_FLIGHT_CONTROL_2(319030, "Dock has no flight control authority."),

    NO_PAYLOAD_CONTROL(319031, "Dock has no payload control authority"),

    WRONG_POINT_NUMBER(319032, "Flyto target point, the point number is wrong."),

    SEQ_NUMBER_SMALLER_THAN_LAST(319033, "DRC - flight control failed. Package sequence number is smaller than last one."),

    DELAY_TIME_SMALLER_THAN_SET(319034, "DRC - flight control failed. Package received time out."),

    EMERGENCY_STOP_FAILED(319035, "Emergency stop failed, please try again."),

    REMOTE_DEBUGGING_MODE(319036, "Device in remote debugging mode. "),

    ONSITE_DEBUGGING_MODE(319037, "Device in onsite debugging mode."),

    UPDATING(319038, "Updating device. Try again later."),

    RESUME_TASK_FAILED(319042, "Failed to resume flight."),

    CANCEL_TASK_FAILED_2(319043, "Failed to cancel RTH."),

    NO_BREAKPOINT(319044, "Task completed. Unable to resume."),

    EMERGENCY_STOP_STATUS(319045, "DRC - flight control failed. Aircraft paused."),

    NOT_IN_WAYLINE(319046, "Task completed or paused. Unable to pause."),

    DOCK_SYSTEM_ERROR_3(319999, "Dock system error. Restart dock and try again."),

    TASK_ERROR(321000, "Task error. Try again later or restart dock and try again."),

    PROCESS_KMZ_FILE_FAILED_2(321004, "Failed to process flight route file. Unable to perform task. Check file."),

    MISSING_BREAKPOINT(321005, "Missing breakpoint info in wayline."),

    TASK_IN_PROGRESS(321257, "Task in progress. Failed to start task again."),

    STATUS_NOT_SUPPORTED(321258, "Unable to stop task. Check aircraft status."),

    NOT_STARTED_CANNOT_STOP(321259, "Task not started. Unable to stop task."),

    NOT_STARTED_CANNOT_INTERRUPT(321260, "Task not started. Unable to pause task."),

    HEIGHT_LIMIT(321513, "Unable to perform task. Flight route altitude greater than aircraft max flight altitude."),

    DISTANCE_LIMIT(321514, "Failed to perform task. Flight route start or end point in buffer zone or exceeds distance limit."),

    GEO_ZONE(321515, "Unable to perform task. Aircraft will fly across GEO Zone."),

    HEIGHT_TOO_LOW(321516, "Flight altitude too low. Task stopped."),

    OBSTACLE_SENSED(321517, "Obstacle sensed. Task stopped."),

    APPROACHED_GEO_ZONE(321519, "Aircraft approached GEO Zone or reached max distance and automatically returned to home. Unable to complete task."),

    PROPELLER_CHECK_FAILED(321523, "Aircraft propeller check failed. Propeller may be damaged. Try again later. Contact DJI Support to replace propeller if issue persists."),

    PREPARATION_FAILED_2(321524, "The preparation before takeoff of the aircraft has failed, possibly due to the aircraft's inability to locate or gear error. Please check the status of the aircraft."),

    WEAK_GPS(321769, "Aircraft satellite positioning signal weak. Unable to perform task. Restart dock and try again."),

    WRONG_GEAR_MODE(321770, "Aircraft flight mode error. Unable to perform task. Restart dock and try again."),

    HOME_POINT_NOT_SET(321771, "Aircraft Home Point not set. Unable to perform task. Restart dock and try again."),

    LOW_POWER_PERFORM_TASK(321772, "Aircraft battery level low. Unable to perform task. Wait until aircraft is charged up to 50% and try again."),

    LOW_POWER_RTH(321773, "Aircraft battery level low and returned to home. Unable to complete task."),

    AIRCRAFT_SIGNAL_LOST(321775, "Aircraft signal lost when performing task."),

    RTK_NOT_READY(321776, "Failed to converge aircraft RTK data. Unable to perform task. Restart dock and try again."),

    NOT_HOVERING(321777, "Aircraft not hovering. Unable to start task."),

    B_CONTROL_PROPELLERS(321778, "Unable to perform task. Aircraft controlled by Controller B, and propellers started."),

    USER_CONTROL(322282, "Task stopped. Aircraft control obtained by cloud user or Controller B."),

    USER_SEND_RTH(322283, "RTH command sent by user. Aircraft unable to complete task."),

    WRONG_BREAKPOINT(322539, "Breakpoint info error. Dock unable to perform task"),

    EMPTY_ACTION_LAYER(322594, "The layer of action tree can not be empty."),

    WRONG_TASK(386535, "Task error. Try again later or restart dock and try again."),

    SET_MEDIA_PRIORITY_FAILED(324030, "Setting priority of media upload failed, the task does not exist in the upload queue."),

    MEDIA_PRIORITY_COMMAND_TOO_FAST(324031, "Setting priority of media upload failed, the action of issuing commands is too fast, and the response to the last command has not yet ended."),

    MEDIA_PRIORITY_WRONG_PARAMETER(324032, "Setting priority of media upload failed, incorrect parameter."),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    WaylineErrorCodeEnum(int code, String msg) {
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

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", message=" + msg +
                '}';
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    @JsonCreator
    public static WaylineErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
