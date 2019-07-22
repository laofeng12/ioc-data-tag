package com.openjava.datatag.schedule.job;

import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zmk
 */
@Component
public class Schedulejob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dtTaggingModelService.setToJob();
    }
}
