package com.dji.sample.common.error;

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
    String getErrorMsg();

    /**
     * Get error code.
     * @return error code
     */
    Integer getErrorCode();

}
