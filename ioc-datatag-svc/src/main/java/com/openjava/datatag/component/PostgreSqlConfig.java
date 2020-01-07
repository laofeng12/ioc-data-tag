package com.openjava.datatag.component;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "pgconfig")
public class PostgreSqlConfig {
    private String username;
    private String password;
    private String ip;
    private String port;
    private String dataBaseName;
    private String pooled;
    private String schema;
}
