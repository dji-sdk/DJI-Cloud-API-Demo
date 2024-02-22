package com.dji.sample.map.service;

import com.dji.sdk.cloudapi.map.ElementCoordinate;

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
    List<ElementCoordinate> getCoordinateByElementId(String elementId);

    /**
     * Save all the coordinate data of this element.
     * @param coordinate
     * @param elementId
     * @return
     */
    Boolean saveCoordinate(List<ElementCoordinate> coordinate, String elementId);

    /**
     * Delete all the coordinates of the element according to its id.
     * @param elementId
     * @return
     */
    Boolean deleteCoordinateByElementId(String elementId);
}
