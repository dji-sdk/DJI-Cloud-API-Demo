package com.dji.sample.map.model.dto;

import lombok.Data;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Data
public class ElementCreateDTO {

    private String id;

    private String name;

    private ElementResourceDTO resource;
}
