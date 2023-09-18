package com.dji.sdk.cloudapi.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/19
 */
@Schema(description = "folder upload callback request data")
public class FolderUploadCallbackRequest {

    @NotNull
    @JsonProperty("file_group_id")
    @Schema(description = "file group id", format = "uuid")
    private String fileGroupId;

    @NotNull
    @JsonProperty("file_count")
    @Schema(description = "total amount of media in the file group")
    private Integer fileCount;

    @NotNull
    @JsonProperty("file_uploaded_count")
    @Schema(description = "the number of uploaded media in the file group")
    private Integer fileUploadedCount;

    public FolderUploadCallbackRequest() {
    }

    @Override
    public String toString() {
        return "FolderUploadCallbackRequest{" +
                "fileGroupId='" + fileGroupId + '\'' +
                ", fileCount=" + fileCount +
                ", fileUploadedCount=" + fileUploadedCount +
                '}';
    }

    public String getFileGroupId() {
        return fileGroupId;
    }

    public FolderUploadCallbackRequest setFileGroupId(String fileGroupId) {
        this.fileGroupId = fileGroupId;
        return this;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public FolderUploadCallbackRequest setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
        return this;
    }

    public Integer getFileUploadedCount() {
        return fileUploadedCount;
    }

    public FolderUploadCallbackRequest setFileUploadedCount(Integer fileUploadedCount) {
        this.fileUploadedCount = fileUploadedCount;
        return this;
    }
}
