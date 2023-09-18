package com.dji.sdk.cloudapi.hms;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
public class DeviceHmsArgs {

    private Long componentIndex;

    private Integer sensorIndex;

    private Integer alarmId;

    public DeviceHmsArgs() {
    }

    @Override
    public String toString() {
        return "DeviceHmsArgs{" +
                "componentIndex=" + componentIndex +
                ", sensorIndex=" + sensorIndex +
                ", alarmId=" + alarmId +
                '}';
    }

    public Long getComponentIndex() {
        return componentIndex;
    }

    public DeviceHmsArgs setComponentIndex(Long componentIndex) {
        this.componentIndex = componentIndex;
        return this;
    }

    public Integer getSensorIndex() {
        return sensorIndex;
    }

    public DeviceHmsArgs setSensorIndex(Integer sensorIndex) {
        this.sensorIndex = sensorIndex;
        return this;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public DeviceHmsArgs setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
        return this;
    }
}
