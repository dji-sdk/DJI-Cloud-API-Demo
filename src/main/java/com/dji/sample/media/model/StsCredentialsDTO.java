package com.dji.sample.media.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StsCredentialsDTO {

    private String bucket;

    private CredentialsDTO credentials;

    private String endpoint;

    private String objectKeyPrefix;

    private String provider;

    private String region;
}
