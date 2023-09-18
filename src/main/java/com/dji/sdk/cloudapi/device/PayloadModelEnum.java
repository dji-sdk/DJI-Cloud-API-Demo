package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/29
 */
public enum PayloadModelEnum {

    Z30("Z30", "20-0"),

    XT2("XT2", "26-0"),

    XTS("XTS", "41-0"),

    H20("H20", "42-0"),

    H20T("H20T", "43-0"),

    P1("P1", "50-65535"),

    M30("M30", "52-0"),

    M30T("M30T", "53-0"),

    H20N("H20N", "61-0"),

    DOCK("DOCK", "165-0"),

    L1("L1", "90742-0");

    public static final String PAYLOAD_KEY = "payload";

    private final String model;

    private final String index;

    PayloadModelEnum(String model, String index) {
        this.model = model;
        this.index = index;
    }

    public String getModel() {
        return model;
    }

    public String getIndex() {
        return index;
    }

    public static Set<String> getAllModelWithPosition() {
        Set<String> position = Arrays.stream(PayloadPositionEnum.values()).map(PayloadPositionEnum::getPosition)
                .map(String::valueOf).collect(Collectors.toSet());
        return Arrays.stream(values()).map(PayloadModelEnum::getModel).map(m -> position.stream().map(p -> m.concat("-").concat(p)))
                .flatMap(Function.identity()).collect(Collectors.toSet());
    }

    public static Set<String> getAllIndexWithPosition() {
        Set<String> position = Arrays.stream(PayloadPositionEnum.values()).map(PayloadPositionEnum::getPosition)
                .map(String::valueOf).collect(Collectors.toSet());
        return Arrays.stream(values()).map(PayloadModelEnum::getIndex).map(m -> position.stream().map(p -> m.concat("-").concat(p)))
                .flatMap(Function.identity()).collect(Collectors.toSet());
    }

    public static PayloadModelEnum find(String model) {
        return Arrays.stream(values()).filter(modelEnum -> modelEnum.model.equals(model)).findAny()
            .orElseThrow(() -> new CloudSDKException(PayloadModelEnum.class, model));
    }

}
