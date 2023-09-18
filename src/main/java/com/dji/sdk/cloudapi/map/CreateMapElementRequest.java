package com.dji.sdk.cloudapi.map;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Schema(description = "Create element request data")
public class CreateMapElementRequest {

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "element id", format = "uuid")
    private String id;

    @Schema(description = "element name", example = "PILOT 1")
    @NotNull
    private String name;

    @NotNull
    @Valid
    private ElementResource resource;

    public CreateMapElementRequest() {
    }

    @Override
    public String toString() {
        return "CreateMapElementRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resource=" + resource +
                '}';
    }

    public String getId() {
        return id;
    }

    public CreateMapElementRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateMapElementRequest setName(String name) {
        this.name = name;
        return this;
    }

    public ElementResource getResource() {
        return resource;
    }

    public CreateMapElementRequest setResource(ElementResource resource) {
        this.resource = resource;
        return this;
    }
}
