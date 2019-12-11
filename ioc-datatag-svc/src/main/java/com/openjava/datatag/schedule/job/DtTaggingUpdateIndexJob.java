package com.openjava.datatag.schedule.job;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.tagmodel.domain.DtWaitUpdateIndex;
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
import java.util.List;

/**
 *
 */
public class DtTaggingUpdateIndexJob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtWaitUpdateIndexService dtWaitUpdateIndexService;

    /**
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        List<DtWaitUpdateIndex> waitList =  dtWaitUpdateIndexService.getByRunState(Constants.DT_INDEX_RUN_STATUS_WAIT);
        waitList.forEach(record->{
            record.setRunState(Constants.DT_INDEX_RUN_STATUS_RUNNING);
            dtWaitUpdateIndexService.doSave(record);
        });
        System.out.println(String.format("更新索引任务开始执行,执行时间:{%s}", new Date()));
        dtWaitUpdateIndexService.updateModelIndex(waitList);
        System.out.println(String.format("更新索引任务结束：结束时间:{%s}", new Date()));
    }
}
