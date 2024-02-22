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
@Schema(description = "line geometry")
public class ElementLineStringGeometry extends ElementGeometryType {

    @Schema(example = "LineString")
    @NotNull
    private final String type = ElementResourceTypeEnum.LINE_STRING.getTypeName();

    @Schema(example = "[[113.943109, 22.577378]]")
    @NotNull
    @Size(min = 2)
    private Double[][] coordinates;

    public ElementLineStringGeometry() {
        super();
    }

    @Override
    public List<ElementCoordinate> convertToList() {
        if (this.coordinates.length < 2) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        List<ElementCoordinate> coordinateList = new ArrayList<>();
        for (Double[] coordinate : this.coordinates) {
            coordinateList.add(new ElementCoordinate()
                    .setLongitude(coordinate[0])
                    .setLatitude(coordinate[1]));
        }
        return coordinateList;
    }

    @Override
    public void adapterCoordinateType(List<ElementCoordinate> coordinateList) {
        if (CollectionUtils.isEmpty(coordinateList) || coordinateList.size() < 2) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER);
        }
        this.coordinates = new Double[coordinateList.size()][2];
        for (int i = 0; i < this.coordinates.length; i++) {
            this.coordinates[i][0] = coordinateList.get(i).getLongitude();
            this.coordinates[i][1] = coordinateList.get(i).getLatitude();
        }
    }

    @Override
    public String toString() {
        return "ElementLineStringGeometry{" +
                "coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public Double[][] getCoordinates() {
        return coordinates;
    }

    public ElementLineStringGeometry setCoordinates(Double[][] coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    @Override
    public String getType() {
        return type;
    }
}
