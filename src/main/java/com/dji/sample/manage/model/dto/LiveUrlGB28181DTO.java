package com.dji.sample.manage.model.dto;

import lombok.Data;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@Data
public class LiveUrlGB28181DTO {

    private String serverIP;

    private Integer serverPort;

    private String serverID;

    private String agentID;

    private String agentPassword;

    private Integer localPort;

    private String channel;

}