package com.openjava.datatag.schedule.job;

import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.tagmodel.service.DtWaitUpdateIndexService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import javax.annotation.Resource;
import java.util.Date;

public class DtTaggingUpdateIndexJob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtWaitUpdateIndexService dtWaitUpdateIndexService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println(String.format("更新索引任务开始执行：JobName:{%s},执行时间:{%s}", new Date()));
        dtWaitUpdateIndexService.updateModelIndex();
        System.out.println(String.format("更新索引任务结束：结束时间:{%s}", new Date()));
    }
}
