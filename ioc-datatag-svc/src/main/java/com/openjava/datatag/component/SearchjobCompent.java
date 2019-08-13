package com.openjava.datatag.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "searchjob")
public class SearchjobCompent {
    private String queue;
    private String jobGroup;
    private String jobName;
    private String jobMethod;
}
