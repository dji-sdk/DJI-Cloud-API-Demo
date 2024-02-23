package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveDTO {

    private String url;

    private String username;

    private String password;
}