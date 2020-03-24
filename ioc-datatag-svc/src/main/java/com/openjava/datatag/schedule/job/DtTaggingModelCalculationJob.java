package com.openjava.datatag.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zmk
 */
public class DtTaggingModelCalculationJob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//消息队列
    /**
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        logger.info(String.format("模型调度开始执行：taggingModelId:{%s},执行时间:{%s}", key.getName(), new Date()));
        DtTaggingModel tagModel = dtTaggingModelService.get(Long.valueOf(key.getName()));
        tagModel.setRunState(Constants.DT_MODEL_RUNNING);
        tagModel.setUpdateNum(0L);//
        tagModel.setSuccessNum(0L);//
        dtTaggingModelService.doSave(tagModel);
        //websocket通知前端
        stringRedisTemplate.convertAndSend(Constants.DT_REDIS_MESSAGE_QUEUE_CHANL, JSONObject.toJSONString(tagModel));//缓存消息队列
        dtTaggingModelService.calculation(tagModel);
    }
}
