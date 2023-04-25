package com.dji.sample.control.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAclDTO {

    private List<String> sub;

    private List<String> pub;

    private List<String> all;
}
