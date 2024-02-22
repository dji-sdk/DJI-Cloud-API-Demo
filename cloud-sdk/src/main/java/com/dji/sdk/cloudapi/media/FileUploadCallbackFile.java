package com.dji.sdk.cloudapi.media;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
public class FileUploadCallbackFile {

    private UploadCallbackFileExtension ext;

    private String name;

    private String path;

    private String objectKey;

    private UploadCallbackFileMetadata metadata;

    public FileUploadCallbackFile() {
    }

    @Override
    public String toString() {
        return "FileUploadCallbackFile{" +
                "ext=" + ext +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    public UploadCallbackFileExtension getExt() {
        return ext;
    }

    public FileUploadCallbackFile setExt(UploadCallbackFileExtension ext) {
        this.ext = ext;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileUploadCallbackFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileUploadCallbackFile setPath(String path) {
        this.path = path;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public FileUploadCallbackFile setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public UploadCallbackFileMetadata getMetadata() {
        return metadata;
    }

    public FileUploadCallbackFile setMetadata(UploadCallbackFileMetadata metadata) {
        this.metadata = metadata;
        return this;
    }
}
