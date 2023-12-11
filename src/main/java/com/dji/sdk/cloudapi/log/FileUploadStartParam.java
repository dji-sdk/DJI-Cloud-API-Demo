package com.dji.sdk.cloudapi.log;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public class FileUploadStartParam {

    @NotNull
    @Size(min = 1, max = 2)
    @Valid
    private List<FileUploadStartFile> files;

    public FileUploadStartParam() {
    }

    @Override
    public String toString() {
        return "FileUploadStartParam{" +
                "files=" + files +
                '}';
    }

    public List<FileUploadStartFile> getFiles() {
        return files;
    }

    public FileUploadStartParam setFiles(List<FileUploadStartFile> files) {
        this.files = files;
        return this;
    }
}
