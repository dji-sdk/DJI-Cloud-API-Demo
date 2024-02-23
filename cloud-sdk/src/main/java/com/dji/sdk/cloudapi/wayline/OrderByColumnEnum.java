package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/25
 */
public enum OrderByColumnEnum {

    NAME("name"),

    UPDATE_TIME("update_time"),

    CREATE_TIME("create_time"),

    ;

    private final String column;

    OrderByColumnEnum(String column) {
        this.column = column;
    }

    @JsonValue
    public String getColumn() {
        return column;
    }

    @JsonCreator
    public static OrderByColumnEnum find(String column) {
        return Arrays.stream(values()).filter(columnEnum -> columnEnum.column.equals(column)).findAny()
            .orElseThrow(() -> new CloudSDKException(OrderByColumnEnum.class, column));
    }
}
