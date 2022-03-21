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
public class ElementPointDTO extends ElementType {

    private Double[] coordinates;

    public ElementPointDTO() {
        super(ElementTypeEnum.POINT.getDesc());
    }

    @Override
    public List<ElementCoordinateDTO> convertToList() {
        List<ElementCoordinateDTO> coordinateList = new ArrayList<>();
        coordinateList.add(ElementCoordinateDTO.builder()
                .longitude(this.coordinates[0])
                .latitude(this.coordinates[1])
                .altitude(this.coordinates.length == 3 ? this.coordinates[2] : null)
                .build());
        return coordinateList;
    }

    @Override
    public void adapterCoordinateType(List<ElementCoordinateDTO> coordinateList) {
        if (CollectionUtils.isEmpty(coordinateList)) {
            return;
        }
        this.coordinates = new Double[]{
                coordinateList.get(0).getLongitude(),
                coordinateList.get(0).getLatitude(),
                coordinateList.get(0).getAltitude()
        };
    }
}
