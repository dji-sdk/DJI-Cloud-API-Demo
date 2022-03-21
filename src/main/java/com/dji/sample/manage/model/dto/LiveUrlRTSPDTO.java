package com.dji.sample.manage.model.dto;

import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
public class LiveUrlRTSPDTO {

    private String userName;

    private String password;

    private Integer port;
}