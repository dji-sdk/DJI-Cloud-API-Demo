package com.dji.sdk.cloudapi.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Schema(description = "element resource")
public class ElementResource {

    @NotNull
    @Schema(type = "int", enumAsRef = true)
    private ElementResourceTypeEnum type;

    @Schema(description = "the user who created the element", example = "pilot")
    @JsonProperty(value = "user_name")
    private String username;

    @NotNull
    @Valid
    private ElementContent content;

    public ElementResource() {
    }

    @Override
    public String toString() {
        return "ElementResource{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", content=" + content +
                '}';
    }

    public ElementResourceTypeEnum getType() {
        return type;
    }

    public ElementResource setType(ElementResourceTypeEnum type) {
        this.type = type;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ElementResource setUsername(String username) {
        this.username = username;
        return this;
    }

    public ElementContent getContent() {
        return content;
    }

    public ElementResource setContent(ElementContent content) {
        this.content = content;
        return this;
    }
}
