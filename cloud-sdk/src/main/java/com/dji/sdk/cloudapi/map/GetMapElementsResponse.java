package com.dji.sdk.cloudapi.map;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Schema(description = "element group data")
public class GetMapElementsResponse extends BaseModel {

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "group id", format = "uuid")
    private String id;

    @NotNull
    @Schema(description = "group name", example = "Pilot Share Layer")
    private String name;

    @NotNull
    private GroupTypeEnum type;

    @NotNull
    @Schema(description = "data collection of elements")
    private List<@Valid MapGroupElement> elements;

    @JsonProperty(value = "is_lock")
    @NotNull
    @Schema(description = "Whether the element group is locked.")
    private Boolean lock;

    public GetMapElementsResponse() {
    }

    @Override
    public String toString() {
        return "GetMapElementsResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", elements=" + elements +
                ", lock=" + lock +
                '}';
    }

    public String getId() {
        return id;
    }

    public GetMapElementsResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetMapElementsResponse setName(String name) {
        this.name = name;
        return this;
    }

    public GroupTypeEnum getType() {
        return type;
    }

    public GetMapElementsResponse setType(GroupTypeEnum type) {
        this.type = type;
        return this;
    }

    public List<MapGroupElement> getElements() {
        return elements;
    }

    public GetMapElementsResponse setElements(List<MapGroupElement> elements) {
        this.elements = elements;
        return this;
    }

    public Boolean getLock() {
        return lock;
    }

    public GetMapElementsResponse setLock(Boolean lock) {
        this.lock = lock;
        return this;
    }
}
