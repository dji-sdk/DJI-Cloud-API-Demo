package com.dji.sdk.cloudapi.firmware;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum FirmwareErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    WRONG_TYPE(312001, "Consistency Upgrade was trrigered, but device didn't request."),

    READY_1_FAILED(312002, "Failed to upgrade. Please try again."),

    VALIDATION_FAILED(312003, "Failed to upgrade. Please try again."),

    READY_2_FAILED(312004, "Failed to upgrade. Please try again."),

    WRONG_PROTOCOL(312010, "The upgrade request is different from the API."),

    WRONG_PARAMETER(312012, "Please check the parameters and try again."),

    COMMAND_1_FAILED(312013, "Failed to upgrade. Please try again."),

    UPDATING(312014, "Updating device firmware. Wait until update completed."),

    WORKING(312015, "Device can not upgrade during the flight. Please wait and try again."),

    TRANSMISSION_ERROR(312016, "Update failed. Dock and aircraft transmission error. Restart dock and aircraft and try again."),

    VERSION_CHECK_FAILED(312017, "Failed to check the version."),

    COMMAND_2_FAILED(312018, "Failed to upgrade. Please try again."),

    COMMAND_3_FAILED(312019, "Failed to upgrade. Please try again."),

    COMMAND_4_FAILED(312020, "Failed to upgrade. Please try again."),

    COMMAND_5_FAILED(312021, "Failed to upgrade. Please try again."),

    AIRCRAFT_NOT_FOUND(312022, "Failed to power on aircraft, or aircraft not connected. Check if aircraft is inside dock, battery installed, and dock and aircraft linked."),

    AIRCRAFT_OUTSIDE(312023, "Failed to push driving rods back into place. Unable to update aircraft firmware. Check if emergency stop button is pressed down or driving rods are stuck."),

    COMMAND_6_FAILED(312024, "Failed to upgrade. Please try again."),

    DELETE_FAILED(312025, "Failed to delete old firmware package."),

    DECOMPRESSION_FAILED(312026, "Failed to decompress the offline upgrade package."),

    NO_AIRCRAFT_DETECTED(312027, "Failed to update firmware. Aircraft not detected inside dock."),

    DEVICE_RESTART_1(312028, "Failed to update firmware. Device restarted during update."),

    DEVICE_RESTART_2(312029, "Restarting device. Unable to update firmware."),

    FOURTH_GENERATION_IS_ENABLE(312030, "Aircraft enhanced transmission enabled. Failed to update firmware. Disable 4G transmission and try again."),

    LOW_POWER(312704, "Aircraft battery level too low. Wait until aircraft is charged to above 20% and try again."),

    UNKNOWN(-1, "UNKNOWN"),
    ;


    private final String msg;

    private final int code;

    FirmwareErrorCodeEnum(int code, String msg) {
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
    public static FirmwareErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
