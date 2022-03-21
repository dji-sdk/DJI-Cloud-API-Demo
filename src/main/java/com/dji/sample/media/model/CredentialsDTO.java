package com.dji.sample.media.model;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import io.minio.credentials.Credentials;
import lombok.Data;


/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Data
public class CredentialsDTO {

    private String accessKeyId;

    private String accessKeySecret;

    private Integer expire;

    private String securityToken;

    public CredentialsDTO(Credentials credentials, int expire) {
        this.accessKeyId = credentials.accessKey();
        this.accessKeySecret = credentials.secretKey();
        this.securityToken = credentials.sessionToken();
        this.expire = expire;
    }

    public CredentialsDTO(AssumeRoleResponse.Credentials credentials, long expire) {
        this.accessKeyId = credentials.getAccessKeyId();
        this.accessKeySecret = credentials.getAccessKeySecret();
        this.securityToken = credentials.getSecurityToken();
        this.expire = Math.toIntExact(expire);
    }

    public CredentialsDTO() {
    }
}
