package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.cloudapi.control.Point;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public class ReturnHomeInfo {

    /**
     * The real-time planned return route for the aircraft.
     * Each push is a complete update of the route.
     * There is an complete return path in the array.
     */
    private List<Point> plannedPathPoints;

    /**
     * You can use this field to determine the display mode of the last point in the trajectory.
     * 0 means the last point of the trajectory is located above the return point on the ground.
     * The terminal can display a line connecting the last point of the trajectory to the return point.
     * 1 means the last point of the trajectory is not the return point.
     * The terminal should not display a line connecting the last point of the trajectory to the return point.
     * The reason for not being able to reach the return point could be that the return point is in a restricted zones or inside an obstacle.
     */
    private LastPointTypeEnum lastPointType;

    /**
     * Currently working wayline mission ID
     */
    private String flightId;

    public ReturnHomeInfo() {
    }

    @Override
    public String toString() {
        return "ReturnHomeInfo{" +
                "plannedPathPoints=" + plannedPathPoints +
                ", lastPointType=" + lastPointType +
                ", flightId='" + flightId + '\'' +
                '}';
    }

    public List<Point> getPlannedPathPoints() {
        return plannedPathPoints;
    }

    public ReturnHomeInfo setPlannedPathPoints(List<Point> plannedPathPoints) {
        this.plannedPathPoints = plannedPathPoints;
        return this;
    }

    public LastPointTypeEnum getLastPointType() {
        return lastPointType;
    }

    public ReturnHomeInfo setLastPointType(LastPointTypeEnum lastPointType) {
        this.lastPointType = lastPointType;
        return this;
    }

    public String getFlightId() {
        return flightId;
    }

    public ReturnHomeInfo setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
