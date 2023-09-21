package com.dji.sdk.cloudapi.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "media fast upload request data")
public class MediaUploadCallbackRequest {

    @NotNull
    @Valid
    private MediaFileExtension ext;

    @NotNull
    @Schema(description = "media file fingerprint", example = "7F78C9F1999425CB61F10E1FE206009E")
    private String fingerprint;

    @NotNull
    @Schema(description = "media file name", example = "DJI_20220831151616_0004_W_Waypoint4.JPG")
    private String name;

    @Schema(description = "media file path. This value is empty if the photo was not taken in the wayline.", example = "DJI_202208311455_008_Waypoint1")
    private String path;

    @JsonProperty("object_key")
    @NotNull
    @Schema(description = "The key of the object in the bucket", example = "media/DJI_20220831151616_0004_W_Waypoint4.JPG")
    private String objectKey;

    @Schema(type = "int")
    @JsonProperty("sub_file_type")
    @NotNull
    private MediaSubFileTypeEnum subFileType;

    @Valid
    @NotNull
    private MediaFileMetadata metadata;

    public MediaUploadCallbackRequest() {
    }

    @Override
    public String toString() {
        return "MediaUploadCallbackRequest{" +
                "ext=" + ext +
                ", fingerprint='" + fingerprint + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", subFileType=" + subFileType +
                ", metadata=" + metadata +
                '}';
    }

    public MediaFileExtension getExt() {
        return ext;
    }

    public MediaUploadCallbackRequest setExt(MediaFileExtension ext) {
        this.ext = ext;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public MediaUploadCallbackRequest setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaUploadCallbackRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public MediaUploadCallbackRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public MediaUploadCallbackRequest setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public MediaSubFileTypeEnum getSubFileType() {
        return subFileType;
    }

    public MediaUploadCallbackRequest setSubFileType(MediaSubFileTypeEnum subFileType) {
        this.subFileType = subFileType;
        return this;
    }

    public MediaFileMetadata getMetadata() {
        return metadata;
    }

    public MediaUploadCallbackRequest setMetadata(MediaFileMetadata metadata) {
        this.metadata = metadata;
        return this;
    }
}
