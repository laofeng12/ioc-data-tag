package com.ioc.datatag.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务例子
 */
@Component
public class OlapJob {



    private Logger logger=LoggerFactory.getLogger(OlapJob.class);

    public void cronJob() throws Exception {
    }



}
