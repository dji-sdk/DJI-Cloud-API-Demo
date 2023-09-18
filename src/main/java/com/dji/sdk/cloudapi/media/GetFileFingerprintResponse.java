package com.dji.sdk.cloudapi.media;


import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/16
 */
@Schema(description = "response data for tiny fingerprints of existing files")
public class GetFileFingerprintResponse extends BaseModel {

    @NotNull
    @Schema(description = "tiny fingerprints collection", example = "[\"297f490b0252690d3f93841818567cc6_2022_8_31_15_16_16\"]")
    @JsonProperty("tiny_fingerprints")
    private List<String> tinyFingerprints;

    public GetFileFingerprintResponse() {
    }

    @Override
    public String toString() {
        return "GetFileFingerprintResponse{" +
                "tinyFingerprints=" + tinyFingerprints +
                '}';
    }

    public List<String> getTinyFingerprints() {
        return tinyFingerprints;
    }

    public GetFileFingerprintResponse setTinyFingerprints(List<String> tinyFingerprints) {
        this.tinyFingerprints = tinyFingerprints;
        return this;
    }
}
