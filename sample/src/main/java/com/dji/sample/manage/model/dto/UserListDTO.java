package com.dji.sample.manage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {

    private String userId;

    private String username;

    private String workspaceName;

    private String userType;

    private String mqttUsername;

    private String mqttPassword;

    private LocalDateTime createTime;
}
