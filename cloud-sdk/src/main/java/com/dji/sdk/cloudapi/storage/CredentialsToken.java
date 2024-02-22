package com.dji.sdk.cloudapi.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Schema(description = "The token data of the temporary credential")
public class CredentialsToken {

    private static final int DELAY = 300;

    @NotNull
    @Schema(description = "access key id", example = "3POX6W77L1EF4C86L2RE")
    @JsonProperty("access_key_id")
    private String accessKeyId;

    @NotNull
    @Schema(description = "access key secret", example = "9NG2P2yJaUrck576CkdRoRbchKssJiZygi5D93CBsduY")
    @JsonProperty("access_key_secret")
    private String accessKeySecret;

    @NotNull
    @Min(1)
    @Schema(description = "The valid time of the token, in seconds.", example = "3600")
    private Long expire;

    @NotNull
    @JsonProperty("security_token")
    @Schema(description = "security token", example = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NLZXkiOiIzUE9YNlc3N0wxRUY0Qzg2TDJSRSIsImV4cCI6MTY4NjgxOTgyOSwicGFyZW50IjoibWluaW8ifQ.cWJXI90UidrBOTD0gWxKt8PT5Qp_6dEK5wNfJuE6lR9dH6Us7jtSB8vcttRDwPhpCNrAGsv91ydw6NLMyjqAOw")
    private String securityToken;

    public CredentialsToken(@NotNull String accessKeyId, @NotNull String accessKeySecret, @NotNull String securityToken, @NotNull @Min(1) Long expire) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.expire = expire - DELAY;
    }

    public CredentialsToken() {
    }

    @Override
    public String toString() {
        return "CredentialsToken{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", expire=" + expire +
                ", securityToken='" + securityToken + '\'' +
                '}';
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public CredentialsToken setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public CredentialsToken setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public Long getExpire() {
        return expire;
    }

    public CredentialsToken setExpire(Long expire) {
        this.expire = expire;
        return this;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public CredentialsToken setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
        return this;
    }
}
