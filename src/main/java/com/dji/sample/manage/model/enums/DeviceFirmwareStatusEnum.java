package com.dji.sample.manage.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/15
 */
@Getter
public enum DeviceFirmwareStatusEnum {

    /**
     * no need to upgrade
     */
    NOT_UPGRADE(1),

    /**
     *  to upgraded
     */
    NORMAL_UPGRADE(2),

    /**
     * A consistency upgrade is required.
     */
    CONSISTENT_UPGRADE(3),

    /**
     * during upgrade
     */
    UPGRADING(4),

    UNKNOWN(-1);

    int val;

    DeviceFirmwareStatusEnum(int val) {
        this.val = val;
    }

    public static DeviceFirmwareStatusEnum find(int val) {
        return Arrays.stream(DeviceFirmwareStatusEnum.values())
                .filter(firmwareStatus -> firmwareStatus.val == val)
                .findFirst().orElse(UNKNOWN);
    }

    @Getter
    public enum CompatibleStatusEnum {
        INCONSISTENT(1),

        CONSISTENT(0);

        int val;

        CompatibleStatusEnum(int val) {
            this.val = val;
        }
    }

}
