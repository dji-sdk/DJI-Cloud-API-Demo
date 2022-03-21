package com.dji.sample.component;

import com.dji.sample.common.model.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/1
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * Please do not return directly like this, there is a risk.
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseResult.error(e.getLocalizedMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseResult nullPointerExceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return ResponseResult.error("A null object appeared.");
    }
}
