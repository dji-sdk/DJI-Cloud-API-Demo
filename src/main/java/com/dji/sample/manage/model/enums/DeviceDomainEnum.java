package com.dji.sample.manage.model.enums;

/**
 *
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
public enum DeviceDomainEnum {

    SUB_DEVICE(0, "sub-device"),

    GATEWAY(2, "gateway"),

    PAYLOAD(1, "payload"),

    DOCK (3, "dock"),

    UNKNOWN(-1, "unknown");

    private int val;

    private String desc;

    DeviceDomainEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(int val) {
        if (SUB_DEVICE.val == val) {
            return SUB_DEVICE.desc;
        }

        if (GATEWAY.val == val) {
            return GATEWAY.desc;
        }

        if (PAYLOAD.val == val) {
            return PAYLOAD.desc;
        }

        if (DOCK.val == val) {
            return DOCK.desc;
        }
        return UNKNOWN.desc;
    }

    public static int getVal(String desc) {
        if (SUB_DEVICE.desc.equals(desc)) {
            return SUB_DEVICE.val;
        }

        if (GATEWAY.desc.equals(desc)) {
            return GATEWAY.val;
        }

        if (PAYLOAD.desc.equals(desc)) {
            return PAYLOAD.val;
        }

        if (DOCK.desc.equals(desc)) {
            return DOCK.val;
        }
        return UNKNOWN.val;
    }


}
