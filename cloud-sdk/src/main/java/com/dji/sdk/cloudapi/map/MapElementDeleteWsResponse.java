package com.dji.sdk.cloudapi.map;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/19
 */
@Schema(description = "BizCode: map_element_delete. <p>Websocket response data when element is deleted.</p>")
public class MapElementDeleteWsResponse extends BaseModel {

    @JsonProperty("group_id")
    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "group id", format = "uuid")
    private String groupId;

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "element id", format = "uuid")
    private String id;

    public MapElementDeleteWsResponse() {
    }

    @Override
    public String toString() {
        return "MapElementDeleteWsResponse{" +
                "groupId='" + groupId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getGroupId() {
        return groupId;
    }

    public MapElementDeleteWsResponse setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getId() {
        return id;
    }

    public MapElementDeleteWsResponse setId(String id) {
        this.id = id;
        return this;
    }
}
