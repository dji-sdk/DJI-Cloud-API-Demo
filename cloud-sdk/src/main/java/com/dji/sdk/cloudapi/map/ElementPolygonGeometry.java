package com.dji.sdk.cloudapi.map;

import com.dji.sdk.exception.CloudSDKErrorEnum;
import com.dji.sdk.exception.CloudSDKException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Schema(description = "polygon geometry")
public class ElementPolygonGeometry extends ElementGeometryType {

    @Schema(example = "Polygon")
    @NotNull
    private final String type = ElementTypeEnum.POLYGON.getDesc();

    @Schema(example = "[[[113.943109, 22.577378]]]")
    @NotNull
    @Size(min = 1, max = 1)
    private Double[][][] coordinates;

    public ElementPolygonGeometry() {
        super();
    }

    @Override
    public List<ElementCoordinate> convertToList() {
        if (this.coordinates[0].length < 3) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        List<ElementCoordinate> coordinateList = new ArrayList<>();
        for (Double[] coordinate : this.coordinates[0]) {
            coordinateList.add(new ElementCoordinate()
                    .setLongitude(coordinate[0])
                    .setLatitude(coordinate[1]));
        }
        return coordinateList;
    }

    @Override
    public void adapterCoordinateType(List<ElementCoordinate> coordinateList) {
        if (CollectionUtils.isEmpty(coordinateList) || coordinateList.size() < 3) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        this.coordinates = new Double[1][coordinateList.size()][2];
        for (int i = 0; i < this.coordinates[0].length; i++) {
            this.coordinates[0][i][0] = coordinateList.get(i).getLongitude();
            this.coordinates[0][i][1] = coordinateList.get(i).getLatitude();
        }
    }

    @Override
    public String toString() {
        return "ElementPolygonGeometry{" +
                "coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public Double[][][] getCoordinates() {
        return coordinates;
    }

    public ElementPolygonGeometry setCoordinates(Double[][][] coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    @Override
    public String getType() {
        return type;
    }
}
