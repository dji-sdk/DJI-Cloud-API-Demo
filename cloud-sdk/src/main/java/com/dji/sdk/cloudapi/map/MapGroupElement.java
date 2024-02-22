package com.dji.sdk.cloudapi.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Schema(description = "element data")
public class MapGroupElement {

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

    public MapGroupElement() {
    }

    @Override
    public String toString() {
        return "MapGroupElement{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", resource=" + resource +
                '}';
    }

    public String getId() {
        return id;
    }

    public MapGroupElement setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MapGroupElement setName(String name) {
        this.name = name;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public MapGroupElement setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public MapGroupElement setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public ElementResource getResource() {
        return resource;
    }

    public MapGroupElement setResource(ElementResource resource) {
        this.resource = resource;
        return this;
    }
}
