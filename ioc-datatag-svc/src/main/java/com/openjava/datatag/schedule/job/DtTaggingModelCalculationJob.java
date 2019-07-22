package com.openjava.datatag.schedule.job;

import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zmk
 */
public class DtTaggingModelCalculationJob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        logger.info(String.format("模型调度开始执行：taggingModelId:{%s},执行时间:{%s}", key.getName(), new Date()));
        dtTaggingModelService.calculation(Long.valueOf(key.getName()));
    }
}
