package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/8
 */
@Schema(description = "Query parameter to get list of wayline files")
public class GetWaylineListRequest {

    /**
     * Is the wayline file favorited?
     */
    @Parameter(name = "favorited", description = "Is the wayline file favorited?")
    private Boolean favorited;

    /**
     * order（xxx_column desc or xxx_column asc)
     * Pilot2 optional value: <span>name</span><span>update_time</span><span>create_time</span>
     */
    @NotNull
    @JsonProperty("order_by")
    @Parameter(name = "order_by", description = "sort field name", example = "update_time desc",
            schema = @Schema(allowableValues = {"name desc", "name asc", "update_time desc", "update_time asc", "create_time desc", "create_time asc"}))
    @Valid
    private GetWaylineListOrderBy orderBy;

    /**
     * current page
     */
    @Min(1)
    @Parameter(name = "page", description = "current page", schema = @Schema(defaultValue = "1", type = "int"))
    private int page = 1;

    /**
     * page size
     */
    @Min(1)
    @JsonProperty("page_size")
    @Parameter(name = "page_size", description = "page size", schema = @Schema(defaultValue = "10", type = "int"))
    private int pageSize = 10;

    /**
     * wayline template type collection
     */
    @Size(min = 1)
    @JsonProperty("template_type")
    @Parameter(name = "template_type", description = "wayline template type collection", example = "[0]")
    private List<WaylineTypeEnum> templateType;

    /**
     * 1: Enable AI Spot-Check wayline. Without this field means all waylines.
     */
    @JsonProperty("action_type")
    @Parameter(name = "action_type", description = "wayline template type collection", example = "1")
    private ActionTypeEnum actionType;

    /**
     * Selected aircraft models
     */
    @JsonProperty("drone_model_keys")
    @Schema(name = "drone_model_keys", description = "drone device product enum", example = "[\"0-67-0\"]")
    private List<DeviceEnum> droneModelKeys;

    /**
     * Selected payload models
     */
    @JsonProperty("payload_model_key")
    @Schema(name = "payload_model_key", description = "payload device product enum", example = "[\"1-53-0\"]")
    private List<DeviceEnum> payloadModelKey;

    /**
     * Filter by wayline name
     */
    @JsonProperty("key")
    @Schema(name = "key", description = "wayline file name", example = "waypoint")
    private String key;

    public GetWaylineListRequest() {
    }

    @Override
    public String toString() {
        return "GetWaylineListRequest{" +
                "favorited=" + favorited +
                ", orderBy='" + orderBy + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", templateType=" + templateType +
                ", actionType=" + actionType +
                ", droneModelKeys=" + droneModelKeys +
                ", payloadModelKey=" + payloadModelKey +
                ", key='" + key + '\'' +
                '}';
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public GetWaylineListRequest setFavorited(Boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public GetWaylineListOrderBy getOrderBy() {
        return orderBy;
    }

    public GetWaylineListRequest setOrderBy(GetWaylineListOrderBy orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public int getPage() {
        return page;
    }

    public GetWaylineListRequest setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public GetWaylineListRequest setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<WaylineTypeEnum> getTemplateType() {
        return templateType;
    }

    public GetWaylineListRequest setTemplateType(List<WaylineTypeEnum> templateType) {
        this.templateType = templateType;
        return this;
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }

    public GetWaylineListRequest setActionType(ActionTypeEnum actionType) {
        this.actionType = actionType;
        return this;
    }

    public List<DeviceEnum> getDroneModelKeys() {
        return droneModelKeys;
    }

    public GetWaylineListRequest setDroneModelKeys(List<DeviceEnum> droneModelKeys) {
        this.droneModelKeys = droneModelKeys;
        return this;
    }

    public List<DeviceEnum> getPayloadModelKey() {
        return payloadModelKey;
    }

    public GetWaylineListRequest setPayloadModelKey(List<DeviceEnum> payloadModelKey) {
        this.payloadModelKey = payloadModelKey;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetWaylineListRequest setKey(String key) {
        this.key = key;
        return this;
    }
}
