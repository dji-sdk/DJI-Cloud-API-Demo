package com.dji.sample.manage.model.enums;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/2
 */
public enum UserTypeEnum {

    WEB(1),

    PILOT(2);

    private int val;


    UserTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
