package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("user_id")
    private String userId;

    private String username;

    @JsonProperty("workspace_id")
    private String workspaceId;

    @JsonProperty("user_type")
    private Integer userType;

    @JsonProperty("mqtt_username")
    private String mqttUsername;

    @JsonProperty("mqtt_password")
    private String mqttPassword;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("mqtt_addr")
    private String mqttAddr;
}
