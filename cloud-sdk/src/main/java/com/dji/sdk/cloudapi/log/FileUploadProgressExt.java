package com.dji.sdk.cloudapi.log;

import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
public class FileUploadProgressExt {

    private List<FileUploadProgressFile> files;

    public FileUploadProgressExt() {
    }

    @Override
    public String toString() {
        return "FileUploadProgressExt{" +
                "files=" + files +
                '}';
    }

    public List<FileUploadProgressFile> getFiles() {
        return files;
    }

    public FileUploadProgressExt setFiles(List<FileUploadProgressFile> files) {
        this.files = files;
        return this;
    }
}
