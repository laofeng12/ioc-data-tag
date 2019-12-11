package com.openjava.datatag.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "platform")
public class platformConfig {
    private String baseUrl;
    private String spUnifyWorkform;
    private String finish;
    private String spUnifyMsgNotice;
}
