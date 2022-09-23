package com.dji.sample.manage.model.dto;

import com.dji.sample.manage.model.receiver.LogsFileUploadList;
import com.dji.sample.media.model.CredentialsDTO;
import com.dji.sample.media.model.StsCredentialsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogsUploadCredentialsDTO {

    private String bucket;

    private CredentialsDTO credentials;

    private String endpoint;

    @JsonProperty("file_store_dir")
    private String objectKeyPrefix;

    private String provider;

    private String fileType = "text_log";

    private LogsFileUploadList params;

    public LogsUploadCredentialsDTO(StsCredentialsDTO sts) {
        this.bucket = sts.getBucket();
        Long expire = sts.getCredentials().getExpire();
        sts.getCredentials().setExpire(System.currentTimeMillis() + (expire - 60) * 1000);
        this.credentials = sts.getCredentials();
        this.endpoint = sts.getEndpoint();
        this.objectKeyPrefix = sts.getObjectKeyPrefix();
        this.provider = sts.getProvider();
    }
}
