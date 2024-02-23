package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.ObstacleAvoidance;
import com.dji.sdk.cloudapi.device.OsdDockDrone;
import com.dji.sdk.cloudapi.device.SwitchActionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ObstacleAvoidanceReceiver extends BasicDeviceProperty {

    private SwitchActionEnum horizon;

    private SwitchActionEnum upside;

    private SwitchActionEnum downside;

    public boolean valid() {
        return Objects.nonNull(this.horizon) || Objects.nonNull(this.upside) || Objects.nonNull(this.downside);
    }

    @Override
    public boolean canPublish(OsdDockDrone osd) {
        ObstacleAvoidance obstacleAvoidance = osd.getObstacleAvoidance();
        return (Objects.nonNull(obstacleAvoidance.getHorizon()) && horizon != obstacleAvoidance.getHorizon())
                || (Objects.nonNull(obstacleAvoidance.getUpside()) && upside != obstacleAvoidance.getUpside())
                || (Objects.nonNull(obstacleAvoidance.getDownside()) && downside != obstacleAvoidance.getDownside());
    }
}
