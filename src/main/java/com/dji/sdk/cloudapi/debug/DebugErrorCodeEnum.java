package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum DebugErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    AIRCRAFT_NO_DONGLE(326002, "The DJI Cellular module is not installed on the aircraft."),

    AIRCRAFT_DONGLE_NO_SIM(326003, "There is no SIM card installed in the DJI Cellular module of the aircraft."),

    AIRCRAFT_DONGLE_NEED_UPGRADE(326004, "The DJI Cellular module of the aircraft needs to be upgraded, otherwise it cannot be used."),

    ESTABLISH_CONNECTION_FAILED(326005, "The 4G transmission of the aircraft fails to be enabled, and the 4G transmission cannot establish a connection. Please check the 4G signal strength, or consult the operator to check the package traffic and APN settings."),

    SDR_SWITCH_FAILED(326006, "The 4G transmission switch failed, please try again later."),

    WRONG_COMMAND_FORMAT(326007, "The command format is wrong."),

    DOCK_NO_DONGLE(326008, "The DJI Cellular module is not installed on the dock."),

    DOCK_DONGLE_NO_SIM(326009, "There is no SIM card installed in the DJI Cellular module of the dock."),

    DOCK_DONGLE_NEED_UPGRADE(326010, "The DJI Cellular module of the dock needs to be upgraded, otherwise it cannot be used."),

    COMMAND_NOT_SUPPORTED(514100, "Dock error. Restart dock and try again."),

    PUSH_DRIVING_RODS_FAILED(514101, "Failed to push driving rods into place."),

    PULL_DRIVING_RODS_FAILED(514102, "Failed to pull driving rods back."),

    LOW_POWER_1(514103, "Aircraft battery level low. Unable to perform task. Wait until aircraft is charged up to 50% and try again."),

    CHARGE_FAILED(514104, "Failed to charge battery."),

    STOP_CHARGING_FAILED(514105, "Failed to stop charging battery."),

    REBOOT_DRONE_FAILED(514106, "Failed to reboot drone."),

    OPEN_DOCK_COVER_FAILED(514107, "Failed to open dock cover."),

    CLOSE_DOCK_COVER_FAILED(514108, "Failed to close dock cover."),

    POWER_ON_AIRCRAFT_FAILED(514109, "Failed to power on aircraft."),

    POWER_OFF_AIRCRAFT_FAILED(514110, "Failed to power off aircraft."),

    OPEN_SLOW_MOTION_FAILED(514111, "Propeller error in opening slow motion mode"),

    CLOSE_SLOW_MOTION_FAILED(514112, "Propeller error in closing slow motion mode"),

    AIRCRAFT_NOT_FOUND_1(514113, "Connection error between driving rod and aircraft. Check if aircraft is inside dock, driving rods are stuck, or charging connector is stained or damaged."),

    OBTAIN_BATTERY_FAILED(514114, "Failed to obtain aircraft battery status. Restart dock and try again."),

    DOCK_BUSY(514116, "Unable to perform operation. Dock is executing other command. Try again later."),

    OBTAIN_DOCK_COVER_FAILED(514117, "Dock cover is open or not fully closed. Restart dock and try again"),

    OBTAIN_DRIVING_RODS_FAILED(514118, "Driving rods pulled back or not pushed into place. Restart dock and try again."),

    TRANSMISSION_ERROR(514120, "Dock and aircraft disconnected. Restart dock and try again or relink dock and aircraft."),

    EMERGENCY_BUTTON_PRESSED_DOWN(514121, "Emergency stop button pressed down. Release button."),

    OBTAIN_CHARGING_STATUS_FAILED(514122, "Failed to obtain aircraft charging status. Restart dock and try again."),

    LOW_POWER_2(514123, "Aircraft battery level too low. Unable to power on aircraft."),

    OBTAIN_BATTERY_STATUS_FAILED(514124, "Failed to obtain aircraft battery information."),

    BATTERY_FULL(514125, "Aircraft battery level almost full. Unable to start charging. Charge battery when battery level is lower than 95%."),

    HEAVY_RAINFALL(514134, "Heavy rainfall. Unable to perform task. Try again later."),

    HIGH_WIND(514135, "Wind speed too high (≥12 m/s). Unable to perform task. Try again later."),

    POWER_SUPPLY_ERROR(514136, "Dock power supply error. Unable to perform task. Resume power supply and try again."),

    LOW_ENVIRONMENT_TEMPERATURE(514137, "Environment temperature too low (lower than -20° C). Unable to perform task. Try again later."),

    BATTERY_MAINTAINING(514138, "Maintaining aircraft battery. Unable to perform task. Wait until maintenance is complete."),

    MAINTAIN_BATTERY_FAILED(514139, "Failed to maintain aircraft battery. No maintenance required."),

    SETTING_BATTERY_STORAGE_FAILED(514140, "Failed to set battery storage mode."),

    DOCK_SYSTEM_ERROR(514141, "Dock system error. Restart dock and try again."),

    AIRCRAFT_NOT_FOUND_2(514142, "Connection error between driving rod and aircraft before takeoff. Check if aircraft is inside dock, driving rods are stuck, or charging connector is stained or damaged."),

    DRIVING_RODS_ERROR(514143, "Driving rods pulled back or not pushed into place. Try again later or restart dock and try again."),

    DOCK_COVER_ERROR(514144, "Dock cover is open or not fully closed."),

    ONSITE_DEBUGGING_MODE(514145, "Dock in onsite debugging mode. Unable to perform current operation or task."),

    REMOTE_DEBUGGING_MODE(514146, "Dock in remote debugging mode. Unable to perform task."),

    FIRMWARE_UPDATING(514147, "Updating device firmware. Unable to perform task."),

    WORKING(514148, "Task in progress. Dock unable to enter remote debugging mode or perform task again. "),

    WRONG_STATUS(514149, "The airport is not in operation mode, but an operation mode-related command has been issued."),

    RESTARTING(514150, "Restarting device."),

    UPDATING(514151, "Updating device firmware."),

    NOT_REMOTE_DEBUGGING_MODE(514153, "Dock exited remote debugging mode. Unable to perform current operation."),

    INITIALIZING(514170, "Initializing dock. Unable to perform operation. Wait until initialization completes."),

    WRONG_PARAMETER(514171, "Cloud command parameter error. Dock unable to execute command."),

    DISABLE_AC_FAILED(514180, "Failed to disable AC cooling or heating."),

    ENABLE_AC_COOLING_FAILED(514181, "Failed to enable AC cooling."),

    ENABLE_AC_HEATING_FAILED(514182, "Failed to enable AC heating."),

    ENABLE_AC_DEHUMIDIFYING_FAILED(514183, "Failed to enable AC dehumidifying."),

    LOW_TEMPERATURE(514184, "Ambient temperature below 0° C. Unable to enable AC cooling."),

    HIGH_TEMPERATURE(514185, "Ambient temperature above 45° C. Unable to enable AC heating"),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    DebugErrorCodeEnum(int code, String msg) {
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
    public static DebugErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
