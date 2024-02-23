package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public enum ThermalPaletteStyleEnum {

    WHITE_HOT(0),

    BLACK_HOT(1),

    RED_HOT(2),

    GREEN_HOT(3),

    FUSION(4),

    RAINBOW(5),

    IRONBOW1(6),

    IRONBOW2(7),

    ICE_FIRE(8),

    SEPIA(9),

    GLOWBOW(10),

    COLOR1(11),

    COLOR2(12),

    RAIN(13),

    HOT_SPOT(14),

    RAINBOW2(15),

    GRAY(16),

    METAL(17),

    COLD_SPOT(18),
    ;

    private final int style;

    ThermalPaletteStyleEnum(int style) {
        this.style = style;
    }

    @JsonValue
    public int getStyle() {
        return style;
    }

    @JsonCreator
    public static ThermalPaletteStyleEnum find(int style) {
        return Arrays.stream(values()).filter(styleEnum -> styleEnum.style == style).findAny()
            .orElseThrow(() -> new CloudSDKException(ThermalPaletteStyleEnum.class, style));
    }

}
