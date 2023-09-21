package com.dji.sdk.cloudapi.log;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class FileUploadListResponse {

    private List<FileUploadListFile> files;

    public FileUploadListResponse() {
    }

    @Override
    public String toString() {
        return "FileUploadListResponse{" +
                "files=" + files +
                '}';
    }

    public List<FileUploadListFile> getFiles() {
        return files;
    }

    public FileUploadListResponse setFiles(List<FileUploadListFile> files) {
        this.files = files;
        return this;
    }
}
