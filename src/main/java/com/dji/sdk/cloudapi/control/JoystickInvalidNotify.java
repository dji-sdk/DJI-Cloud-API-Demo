package com.dji.sdk.cloudapi.control;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public class JoystickInvalidNotify {

    private JoystickInvalidReasonEnum reason;

    public JoystickInvalidNotify() {
    }

    @Override
    public String toString() {
        return "JoystickInvalidNotify{" +
                "reason=" + reason +
                '}';
    }

    public JoystickInvalidReasonEnum getReason() {
        return reason;
    }

    public JoystickInvalidNotify setReason(JoystickInvalidReasonEnum reason) {
        this.reason = reason;
        return this;
    }
}
