package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public enum ShutterSpeedEnum {

    /**
     * 1/8000 s
     */
    _1_8000TH_S(0),

    _1_6400TH_S(1),

    _1_6000TH_S(2),

    _1_5000TH_S(3),

    _1_4000TH_S(4),

    _1_3200TH_S(5),

    _1_3000TH_S(6),

    _1_2500TH_S(7),

    _1_2000TH_S(8),

    _1_1600TH_S(9),

    _1_1500TH_S(10),

    _1_1250TH_S(11),

    _1_1000TH_S(12),

    _1_800TH_S(13),

    _1_725TH_S(14),

    _1_640TH_S(15),

    _1_500TH_S(16),

    _1_400TH_S(17),

    _1_350TH_S(18),

    _1_320TH_S(19),

    _1_250TH_S(20),

    _1_240TH_S(21),

    _1_200TH_S(22),

    _1_180TH_S(23),

    _1_160TH_S(24),

    _1_125TH_S(25),

    _1_120TH_S(26),

    _1_100TH_S(27),

    _1_90TH_S(28),

    _1_80TH_S(29),

    _1_60TH_S(30),

    _1_50TH_S(31),

    _1_40TH_S(32),

    _1_30TH_S(33),

    _1_25TH_S(34),

    _1_20TH_S(35),

    _1_15TH_S(36),

    _2_25THS_S(37),

    _1_10TH_S(38),

    _1_8TH_S(39),

    _4_25THS_S(40),

    _1_5TH_S(41),

    _1_4TH_S(42),

    _1_3TH_S(43),

    _2_5THS_S(44),

    _1_2TH_S(45),

    /**
     * 3/5 s
     */
    _3_5THS_S(46),

    _4_5TH_S(47),

    /**
     * 1 s
     */
    _1S(48),

    /**
     * 1.3 s
     */
    _1_DOT_3S(49),

    _1_DOT_6_S(50),

    _2_S(51),

    _2_DOT_5_S(52),

    _3_DOT_S(53),

    _3_DOT_2_S(54),

    _4_DOT_S(55),

    _5_DOT_S(56),

    _6_DOT_S(57),

    _7_DOT_S(58),

    _8_DOT_S(59),

    AUTO(65534),

    ;

    private final int speed;

    ShutterSpeedEnum(int speed) {
        this.speed = speed;
    }

    @JsonValue
    public int getSpeed() {
        return speed;
    }

    @JsonCreator
    public static ShutterSpeedEnum find(int speed) {
        return Arrays.stream(values()).filter(speedEnum -> speedEnum.speed == speed).findAny()
            .orElseThrow(() -> new CloudSDKException(ShutterSpeedEnum.class, speed));
    }

}
