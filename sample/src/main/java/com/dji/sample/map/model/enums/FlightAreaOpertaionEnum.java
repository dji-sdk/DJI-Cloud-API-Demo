package com.dji.sample.map.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/1
 */
public enum FlightAreaOpertaionEnum {

    ADD("add"),

    UPDATE("update"),

    DELETE("delete")

    ;

    private final String operation;

    FlightAreaOpertaionEnum(String operation) {
        this.operation = operation;
    }

    @JsonValue
    public String getOperation() {
        return operation;
    }

    @JsonCreator
    public static FlightAreaOpertaionEnum find(String operation) {
        return Arrays.stream(values()).filter(operationEnum -> operationEnum.operation.equals(operation)).findAny()
            .orElseThrow(() -> new RuntimeException("This operation(" + operation + ") is not supported."));
    }
}
