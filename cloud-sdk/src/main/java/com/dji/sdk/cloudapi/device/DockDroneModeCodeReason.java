package com.dji.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class DockDroneModeCodeReason {

    private ModeCodeReasonEnum modeCodeReason;

    public DockDroneModeCodeReason() {
    }

    @Override
    public String toString() {
        return "DockDroneModeCodeReason{" +
                "modeCodeReason=" + modeCodeReason +
                '}';
    }

    public ModeCodeReasonEnum getModeCodeReason() {
        return modeCodeReason;
    }

    public DockDroneModeCodeReason setModeCodeReason(ModeCodeReasonEnum modeCodeReason) {
        this.modeCodeReason = modeCodeReason;
        return this;
    }
}
