package com.dji.sdk.cloudapi.map;

import com.dji.sdk.cloudapi.flightarea.GeometrySubTypeEnum;

import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
public class ElementCircleGeometry extends ElementPointGeometry {

    private final String type = GeometrySubTypeEnum.CIRCLE.getSubType();

    private Float radius;

    public ElementCircleGeometry() {
    }

    @Override
    public String toString() {
        return "ElementCircleGeometry{" +
                "type='" + type + '\'' +
                ", radius=" + radius +
                '}';
    }

    @Override
    public List<ElementCoordinate> convertToList() {
        return super.convertToList();
    }

    @Override
    public void adapterCoordinateType(List<ElementCoordinate> coordinateList) {
        super.adapterCoordinateType(coordinateList);
        Double[] coordinates = this.getCoordinates();
        this.setCoordinates(new Double[]{coordinates[0], coordinates[1]});
    }

    @Override
    public Double[] getCoordinates() {
        return super.getCoordinates();
    }

    @Override
    public ElementPointGeometry setCoordinates(Double[] coordinates) {
        return super.setCoordinates(coordinates);
    }

    @Override
    public String getType() {
        return type;
    }

    public Float getRadius() {
        return radius;
    }

    public ElementCircleGeometry setRadius(Float radius) {
        this.radius = radius;
        return this;
    }
}
