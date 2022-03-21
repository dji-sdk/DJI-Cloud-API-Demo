package com.dji.sample.map.service;

import com.dji.sample.map.model.dto.ElementCoordinateDTO;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
public interface IElementCoordinateService {

    /**
     * Query all the coordinates of the element according to its id.
     * @param elementId
     * @return
     */
    List<ElementCoordinateDTO> getCoordinateByElementId(String elementId);

    /**
     * Save all the coordinate data of this element.
     * @param coordinate
     * @param elementId
     * @return
     */
    Boolean saveCoordinate(List<ElementCoordinateDTO> coordinate, String elementId);

    /**
     * Delete all the coordinates of the element according to its id.
     * @param elementId
     * @return
     */
    Boolean deleteCoordinateByElementId(String elementId);
}
