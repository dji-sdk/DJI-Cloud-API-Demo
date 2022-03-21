package com.dji.sample.manage.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WorkspaceDTO {

    public static final String DEFAULT_WORKSPACE_ID = "e3dea0f5-37f2-4d79-ae58-490af3228069";

    private Integer id;

    private String workspaceId;

    private String workspaceName;

    private String workspaceDesc;

    private String platformName;
}