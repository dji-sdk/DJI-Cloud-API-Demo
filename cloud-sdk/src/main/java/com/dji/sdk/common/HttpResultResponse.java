package com.dji.sdk.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The data format of the http response.")
public class HttpResultResponse<T> {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAILED = -1;
    public static final String MESSAGE_SUCCESS = "success";
    public static final String MESSAGE_FAILED = "failed";

    @Schema(description = "0 means success, non-zero means error.", example = "0")
    private int code;

    @Schema(description = "The response message.", example = MESSAGE_SUCCESS)
    private String message;

    @Schema(description = "The response data.")
    private T data;
    
    public HttpResultResponse() {
    }

    @Override
    public String toString() {
        return "HttpResultResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public HttpResultResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HttpResultResponse<T> setMessage(String message) {
        this.message = message;;
        return this;
    }

    public T getData() {
        return data;
    }

    public HttpResultResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static HttpResultResponse success() {
        return new HttpResultResponse()
                .setCode(CODE_SUCCESS)
                .setMessage(MESSAGE_SUCCESS)
                .setData("");
    }

    public static <T> HttpResultResponse<T> success(T data) {
        return HttpResultResponse.success().setData(data);
    }

    public static HttpResultResponse error() {
        return new HttpResultResponse()
                .setCode(CODE_FAILED)
                .setMessage(MESSAGE_FAILED);
    }

    public static HttpResultResponse error(String message) {
        return new HttpResultResponse()
                .setCode(CODE_FAILED)
                .setMessage(message);
    }

    public static HttpResultResponse error(int code, String message) {
        return new HttpResultResponse()
                .setCode(code)
                .setMessage(message);
    }

    public static HttpResultResponse error(IErrorInfo errorInfo) {
        return new HttpResultResponse()
                .setCode(errorInfo.getCode())
                .setMessage(errorInfo.getMessage());
    }
}
