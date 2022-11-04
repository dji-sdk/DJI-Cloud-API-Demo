package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.enums.StateSwitchReceiver;
import lombok.Data;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@Data
public class ObstacleAvoidanceReceiver extends BasicDeviceProperty {

    private Integer horizon;

    private Integer upside;

    private Integer downside;

    @Override
    public boolean valid() {
        boolean valid = Objects.nonNull(this.horizon) || Objects.nonNull(this.upside) || Objects.nonNull(this.downside);

        StateSwitchReceiver stateSwitch = new StateSwitchReceiver();
        if (Objects.nonNull(this.horizon)) {
            stateSwitch.setValue(this.horizon);
            valid = stateSwitch.valid();
        }
        if (Objects.nonNull(this.upside)) {
            stateSwitch.setValue(this.upside);
            valid &= stateSwitch.valid();
        }
        if (Objects.nonNull(this.downside)) {
            stateSwitch.setValue(this.downside);
            valid &= stateSwitch.valid();
        }
        return valid;
    }

    @Override
    public boolean canPublish(String fieldName, OsdSubDeviceReceiver osd) {
        ObstacleAvoidanceReceiver obstacleAvoidance = osd.getObstacleAvoidance();
        switch (fieldName) {
            case "horizon":
                return Objects.isNull(obstacleAvoidance.getHorizon()) ||
                        Objects.nonNull(obstacleAvoidance.getHorizon()) &&
                                obstacleAvoidance.getHorizon().intValue() != this.horizon;
            case "upside":
                return Objects.isNull(obstacleAvoidance.getUpside()) ||
                        Objects.nonNull(obstacleAvoidance.getUpside()) &&
                                obstacleAvoidance.getUpside().intValue() != this.upside;
            case "downside":
                return Objects.isNull(obstacleAvoidance.getDownside()) ||
                        Objects.nonNull(obstacleAvoidance.getDownside()) &&
                                obstacleAvoidance.getDownside().intValue() != this.downside;
            default:
                throw new RuntimeException("Property " + fieldName + " does not exist.");
        }
    }
}
