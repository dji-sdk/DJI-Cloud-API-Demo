package com.dji.sdk.cloudapi.media;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "media fast upload request data")
public class MediaFastUploadRequest {

    @NotNull
    @Valid
    private FastUploadExtension ext;

    @NotNull
    @Schema(description = "media file fingerprint", example = "7F78C9F1999425CB61F10E1FE206009E")
    private String fingerprint;

    @NotNull
    @Schema(description = "media file name", example = "DJI_20220831151616_0004_W_Waypoint4.JPG")
    private String name;

    @Schema(description = "media file path. This value is empty if the photo was not taken in the wayline.", example = "DJI_202208311455_008_Waypoint1")
    private String path;

    public MediaFastUploadRequest() {
    }

    @Override
    public String toString() {
        return "MediaFastUploadRequest{" +
                "ext=" + ext +
                ", fingerprint='" + fingerprint + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public FastUploadExtension getExt() {
        return ext;
    }

    public MediaFastUploadRequest setExt(FastUploadExtension ext) {
        this.ext = ext;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public MediaFastUploadRequest setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public String getName() {
        return name;
    }

    public MediaFastUploadRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public MediaFastUploadRequest setPath(String path) {
        this.path = path;
        return this;
    }
}
