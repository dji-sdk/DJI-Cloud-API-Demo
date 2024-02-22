package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.cloudapi.device.DeviceEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Schema(description = "The data of the wayline file.")
public class GetWaylineListResponse extends BaseModel {

    /**
     * wayline file name
     */
    @NotNull
    @Schema(description = "wayline file name", example = "waylineFile")
    @Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")
    private String name;

    /**
     * wayline file id
     */
    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @Schema(description = "wayline file id", format = "uuid")
    private String id;

    /**
     * drone device product enum
     */
    @NotNull
    @JsonProperty("drone_model_key")
    @Schema(description = "drone device product enum", example = "0-67-0")
    private DeviceEnum droneModelKey;

    private String sign;

    /**
     * payload device product enum
     */
    @NotNull
    @Size(min = 1)
    @JsonProperty("payload_model_keys")
    @Schema(description = "payload device product enum", example = "[\"1-53-0\"]")
    private List<DeviceEnum> payloadModelKeys;

    /**
     * Is the wayline file favorited?
     */
    @NotNull
    @Schema(description = "Is the wayline file favorited?")
    private Boolean favorited;

    /**
     * wayline template collection
     */
    @NotNull
    @Size(min = 1)
    @Schema(description = "wayline template collection", example = "[0]")
    @JsonProperty("template_types")
    private List<WaylineTypeEnum> templateTypes;

    @NotNull
    @Schema(description = "The key of the object in the bucket", example = "wayline/waylineFile.kmz")
    @JsonProperty("object_key")
    private String objectKey;

    /**
     * uploader
     */
    @NotNull
    @JsonProperty("user_name")
    @Schema(description = "uploader's username", example = "admin")
    private String username;

    /**
     * update time (millisecond)
     */
    @NotNull
    @Min(123456789012L)
    @Schema(description = "update time (millisecond). The field named `update time` must exist in the table.", example = "123456789012")
    @JsonProperty("update_time")
    private Long updateTime;

    /**
     * create time (millisecond)
     */
    @NotNull
    @Min(123456789012L)
    @Schema(description = "create time (millisecond). The field named `create time` must exist in the table.", example = "123456789012")
    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("action_type")
    @Parameter(name = "action_type", description = "wayline template type collection", example = "1")
    private ActionTypeEnum actionType;

    public GetWaylineListResponse() {
    }

    @Override
    public String toString() {
        return "GetWaylineListResponse{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", droneModelKey=" + droneModelKey +
                ", sign='" + sign + '\'' +
                ", payloadModelKeys=" + payloadModelKeys +
                ", favorited=" + favorited +
                ", templateTypes=" + templateTypes +
                ", objectKey='" + objectKey + '\'' +
                ", username='" + username + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", actionType=" + actionType +
                '}';
    }

    public String getName() {
        return name;
    }

    public GetWaylineListResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetWaylineListResponse setId(String id) {
        this.id = id;
        return this;
    }

    public DeviceEnum getDroneModelKey() {
        return droneModelKey;
    }

    public GetWaylineListResponse setDroneModelKey(DeviceEnum droneModelKey) {
        this.droneModelKey = droneModelKey;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public GetWaylineListResponse setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public List<DeviceEnum> getPayloadModelKeys() {
        return payloadModelKeys;
    }

    public GetWaylineListResponse setPayloadModelKeys(List<DeviceEnum> payloadModelKeys) {
        this.payloadModelKeys = payloadModelKeys;
        return this;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public GetWaylineListResponse setFavorited(Boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public List<WaylineTypeEnum> getTemplateTypes() {
        return templateTypes;
    }

    public GetWaylineListResponse setTemplateTypes(List<WaylineTypeEnum> templateTypes) {
        this.templateTypes = templateTypes;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public GetWaylineListResponse setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GetWaylineListResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public GetWaylineListResponse setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public GetWaylineListResponse setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }

    public GetWaylineListResponse setActionType(ActionTypeEnum actionType) {
        this.actionType = actionType;
        return this;
    }
}
