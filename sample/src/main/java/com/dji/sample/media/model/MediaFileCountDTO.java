package com.dji.sample.media.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileCountDTO {

    private String tid;

    private String bid;

    private String preJobId;

    private String jobId;

    private Integer mediaCount;

    private Integer uploadedCount;

    private String deviceSn;
}
