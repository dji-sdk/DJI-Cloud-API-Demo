package com.dji.sdk.cloudapi.map;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/19
 */
@Schema(description = "BizCode: map_element_update. <p>Websocket response data when the element is updated.</p>")
public class MapElementUpdateWsResponse extends BaseModel {

    @JsonProperty("group_id")
    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "group id", format = "uuid")
    private String groupId;

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "element id", format = "uuid")
    private String id;

    @NotNull
    @Schema(description = "element name", example = "PILOT 1")
    private String name;

    @NotNull
    @Schema(description = "element create time", example = "123456789012")
    @JsonProperty(value = "create_time")
    @Min(123456789012L)
    private Long createTime;

    @NotNull
    @Schema(description = "element update time", example = "123456789012")
    @JsonProperty(value = "update_time")
    @Min(123456789012L)
    private Long updateTime;

    @NotNull
    @Valid
    private ElementResource resource;

    public MapElementUpdateWsResponse() {
    }

    @Override
    public String toString() {
        return "MapElementUpdateWsResponse{" +
                "groupId='" + groupId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", resource=" + resource +
                '}';
    }

    public String getId() {
        return id;
    }

    public MapElementUpdateWsResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MapElementUpdateWsResponse setName(String name) {
        this.name = name;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public MapElementUpdateWsResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public MapElementUpdateWsResponse setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public ElementResource getResource() {
        return resource;
    }

    public MapElementUpdateWsResponse setResource(ElementResource resource) {
        this.resource = resource;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public MapElementUpdateWsResponse setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

}
