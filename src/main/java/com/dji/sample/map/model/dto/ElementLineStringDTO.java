package com.dji.sample.map.model.dto;

import com.dji.sample.map.model.enums.ElementTypeEnum;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Data
public class ElementLineStringDTO extends ElementType {

    private Double[][] coordinates;

    public ElementLineStringDTO() {
        super(ElementTypeEnum.LINE_STRING.getDesc());
    }

    @Override
    public List<ElementCoordinateDTO> convertToList() {
        List<ElementCoordinateDTO> coordinateList = new ArrayList<>();
        for (Double[] coordinate : this.coordinates) {
            coordinateList.add(ElementCoordinateDTO.builder()
                    .longitude(coordinate[0])
                    .latitude(coordinate[1])
                    .build());
        }
        return coordinateList;
    }

    @Override
    public void adapterCoordinateType(List<ElementCoordinateDTO> coordinateList) {
        if (CollectionUtils.isEmpty(coordinateList)) {
            return;
        }
        this.coordinates = new Double[coordinateList.size()][2];
        for (int i = 0; i < this.coordinates.length; i++) {
            this.coordinates[i][0] = coordinateList.get(i).getLongitude();
            this.coordinates[i][1] = coordinateList.get(i).getLatitude();
        }
    }
}
