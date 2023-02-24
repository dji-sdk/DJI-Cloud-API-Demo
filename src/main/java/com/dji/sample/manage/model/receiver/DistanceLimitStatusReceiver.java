package com.dji.sample.manage.model.receiver;

import com.dji.sample.manage.model.enums.StateSwitchEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceLimitStatusReceiver extends BasicDeviceProperty {

    private Integer state;

    private Integer distanceLimit;

    private static final int DISTANCE_MAX = 8000;

    private static final int DISTANCE_MIN = 15;

    @Override
    public boolean valid() {
        boolean valid = Objects.nonNull(state) || Objects.nonNull(distanceLimit);
        if (Objects.nonNull(state)) {
            valid = StateSwitchEnum.find(state).isPresent();
        }
        if (Objects.nonNull(distanceLimit)) {
            valid &= distanceLimit >= DISTANCE_MIN && distanceLimit <= DISTANCE_MAX;
        }
        return valid;
    }

    @Override
    public boolean canPublish(String fieldName, OsdSubDeviceReceiver osd) {
        DistanceLimitStatusReceiver distanceLimitStatus = osd.getDistanceLimitStatus();
        switch (fieldName) {
            case "state":
                return Objects.isNull(distanceLimitStatus.getState()) ||
                        Objects.nonNull(distanceLimitStatus.getState()) &&
                                distanceLimitStatus.getState().intValue() != this.state;
            case "distance_limit":
                return Objects.isNull(distanceLimitStatus.getDistanceLimit()) ||
                        Objects.nonNull(distanceLimitStatus.getDistanceLimit()) &&
                                distanceLimitStatus.getDistanceLimit().intValue() != this.distanceLimit;
            default:
                throw new RuntimeException("Property " + fieldName + " does not exist.");
        }
    }
}
