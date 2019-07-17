package com.openjava.datatag.schedule.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class Hello3Job implements Job {

    Logger logger = LogManager.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("JobName3: {}", jobExecutionContext.getJobDetail().getKey().getName() + "Hello3Job执行时间: " + new Date());
    }
}
