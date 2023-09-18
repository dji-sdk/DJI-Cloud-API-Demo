package com.dji.sdk.cloudapi.map;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/1
 */
@Schema(description = "Update element request data")
public class UpdateMapElementRequest {

    @Schema(description = "element name", example = "PILOT 1")
    @NotNull
    private String name;

    @NotNull
    @Valid
    private ElementContent content;

    public UpdateMapElementRequest() {
    }

    @Override
    public String toString() {
        return "UpdateMapElementRequest{" +
                "name='" + name + '\'' +
                ", content=" + content +
                '}';
    }

    public String getName() {
        return name;
    }

    public UpdateMapElementRequest setName(String name) {
        this.name = name;
        return this;
    }

    public ElementContent getContent() {
        return content;
    }

    public UpdateMapElementRequest setContent(ElementContent content) {
        this.content = content;
        return this;
    }
}
