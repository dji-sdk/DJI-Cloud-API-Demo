package com.dji.sample.map.service;

import com.dji.sample.map.model.dto.ElementCreateDTO;
import com.dji.sample.map.model.dto.ElementUpdateDTO;
import com.dji.sample.map.model.dto.GroupElementDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
public interface IGroupElementService {

    /**
     * Query all the elements in this group based on the group id.
     * @param groupId
     * @return
     */
    List<GroupElementDTO> getElementsByGroupId(String groupId);

    /**
     * Save all the elements.
     * @param groupId
     * @param elementCreate
     * @return
     */
    Boolean saveElement(String groupId, ElementCreateDTO elementCreate);

    /**
     * Query the element information based on the element id and update element.
     * @param elementId
     * @param elementUpdate
     * @param username
     * @return
     */
    Boolean updateElement(String elementId, ElementUpdateDTO elementUpdate, String username);

    /**
     * Delete the element based on the element id.
     * @param elementId
     * @return
     */
    Boolean deleteElement(String elementId);

    /**
     * Query an element based on the element id, including the coordinate information in the elements.
     * @param elementId
     * @return
     */
    Optional<GroupElementDTO> getElementByElementId(String elementId);
}
