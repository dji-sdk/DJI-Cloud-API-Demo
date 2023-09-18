package com.dji.sample.component;

import com.dji.sdk.common.HttpResultResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public HttpResultResponse exceptionHandler(Exception e) {
        e.printStackTrace();
        return HttpResultResponse.error(e.getLocalizedMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpResultResponse nullPointerExceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return HttpResultResponse.error("A null object appeared.");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public HttpResultResponse methodArgumentNotValidExceptionHandler(BindException e) {
        e.printStackTrace();
        return HttpResultResponse.error(e.getFieldError().getField() + e.getFieldError().getDefaultMessage());
    }

}
