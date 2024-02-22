package com.dji.sample.manage.model.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Component
@ConfigurationProperties("ntp.server")
public class NtpServerProperties {

    public static String host;

    public void setHost(String host) {
        NtpServerProperties.host = host;
    }
}
