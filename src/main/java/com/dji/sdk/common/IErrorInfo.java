package com.dji.sdk.common;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public interface IErrorInfo {

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
