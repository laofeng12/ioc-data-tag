package com.openjava.datatag.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zmk
 */
@Data
@Component
@ConfigurationProperties(prefix = "schedulejob")
public class SchedulejobCompent {
    private String queue;
    private String jobGroup;
    private String jobName;
    private String jobMethod;
}
