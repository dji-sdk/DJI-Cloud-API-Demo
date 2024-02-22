package com.dji.sdk.cloudapi.log;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
public class FileUploadProgress {

    private FileUploadProgressExt ext;

    private FileUploadStatusEnum status;

    public FileUploadProgress() {
    }

    @Override
    public String toString() {
        return "FileUploadProgress{" +
                "ext=" + ext +
                ", status=" + status +
                '}';
    }

    public FileUploadProgressExt getExt() {
        return ext;
    }

    public FileUploadProgress setExt(FileUploadProgressExt ext) {
        this.ext = ext;
        return this;
    }

    public FileUploadStatusEnum getStatus() {
        return status;
    }

    public FileUploadProgress setStatus(FileUploadStatusEnum status) {
        this.status = status;
        return this;
    }
}
