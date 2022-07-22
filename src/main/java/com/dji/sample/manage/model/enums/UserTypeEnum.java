package com.dji.sample.manage.model.enums;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/2
 */
public enum UserTypeEnum {

    WEB(1, "Web"),

    PILOT(2, "Pilot"),

    UNKNOWN(-1, "Unknown");

    private int val;

    private String desc;

    UserTypeEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return this.val;
    }

    public String getDesc() {
        return this.desc;
    }

    public static UserTypeEnum find(int val) {
        if (val == WEB.val) {
            return WEB;
        }
        if (val == PILOT.val) {
            return PILOT;
        }
        return UNKNOWN;
    }
}
