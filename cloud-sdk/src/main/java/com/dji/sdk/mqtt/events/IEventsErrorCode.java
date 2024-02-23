package com.dji.sdk.mqtt.events;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public interface IEventsErrorCode {

    /**
     * Get error message.
     * @return error message
     */
    String getMessage();

    /**
     * Get error code.
     * @return error code
     */
    Integer getCode();

}
