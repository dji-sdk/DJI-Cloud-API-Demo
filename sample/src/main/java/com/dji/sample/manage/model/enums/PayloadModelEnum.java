package com.dji.sample.manage.model.enums;

import java.util.Arrays;
import java.util.List;
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

    private String model;

    private String index;

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

    public static List<String> getAllModel() {
        return Arrays.stream(PayloadModelEnum.values()).map(PayloadModelEnum::getModel).collect(Collectors.toList());
    }

    public static List<String> getAllIndex() {
        return Arrays.stream(PayloadModelEnum.values()).map(PayloadModelEnum::getIndex).collect(Collectors.toList());
    }

}
