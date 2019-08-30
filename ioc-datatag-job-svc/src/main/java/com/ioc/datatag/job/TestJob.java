package com.ioc.datatag.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务例子
 */
@Component
public class TestJob {



    private Logger logger=LoggerFactory.getLogger(TestJob.class);

    public void cronJob() throws Exception {
    }



}
