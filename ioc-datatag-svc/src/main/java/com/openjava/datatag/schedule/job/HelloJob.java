package com.openjava.datatag.schedule.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.util.Date;

public class HelloJob implements Job {

    Logger logger = LogManager.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        logger.info("JobName: {}", key.getName() + " JobGroup: "+ key.getGroup() + " JobClass: "+key.getClass() + " 执行时间: " + new Date());
        //System.out.println("key:"+key);
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Object parameter = dataMap.get("parameter");
        System.out.println("parameter:"+parameter);
    }
}
