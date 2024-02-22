package com.dji.sdk.cloudapi.map;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/16
 */
@Schema(description = "Create element response data")
public class CreateMapElementResponse {

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "element id", format = "uuid")
    private String id;

    public CreateMapElementResponse() {
    }

    @Override
    public String toString() {
        return "CreateMapElementResponse{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public CreateMapElementResponse setId(String id) {
        this.id = id;
        return this;
    }
}
