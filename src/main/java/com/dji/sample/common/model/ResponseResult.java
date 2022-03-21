package com.dji.sample.common.model;

import com.dji.sample.common.error.IErrorInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
public class ResponseResult<T> {

    public static final int CODE_SUCCESS = 0;
    public static final String MESSAGE_SUCCESS = "success";

    private int code;

    private String message;

    private T data;

    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder()
                .code(CODE_SUCCESS)
                .message(MESSAGE_SUCCESS)
                .data(data)
                .build();
    }

    public static ResponseResult success() {
        return ResponseResult.builder()
                .code(0)
                .message(MESSAGE_SUCCESS)
                .build();
    }

    public static ResponseResult error() {
        return ResponseResult.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }

    public static ResponseResult error(String message) {
        return ResponseResult.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message)
                .build();
    }

    public static ResponseResult error(int code, String message) {
        return ResponseResult.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static ResponseResult error(IErrorInfo errorInfo) {
        return ResponseResult.builder()
                .code(errorInfo.getErrorCode())
                .message(errorInfo.getErrorMsg())
                .build();
    }
}
