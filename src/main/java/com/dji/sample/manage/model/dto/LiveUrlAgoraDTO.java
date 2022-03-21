package com.dji.sample.manage.model.dto;

import lombok.Data;

/**
 * @author sean.zhou
 * @date 2021/11/23
 * @version 0.1
 */
@Data
public class LiveUrlAgoraDTO {

    private String channel;

    private String sn;

    private String token;

    private Integer uid;
}