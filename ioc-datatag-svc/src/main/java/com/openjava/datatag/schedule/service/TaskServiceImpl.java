package com.openjava.datatag.schedule.service;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.schedule.domain.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ljdp.component.exception.APIException;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author zmk
 */
@Service
public class TaskServiceImpl implements TaskService{
    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private Scheduler scheduler;

    public String addJob(){
        return "wocaonima";
    }


    /**
    * 所有任务列表
    */
    public List<TaskInfo> list() throws Exception{
        List<TaskInfo> list = new ArrayList<>();
        for(String groupJob: scheduler.getJobGroupNames()){
            for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))){
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger: triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                    String cronExpression = "", createTime = "";

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        cronExpression = cronTrigger.getCronExpression();
                        createTime = cronTrigger.getDescription();
                    }
                    TaskInfo info = new TaskInfo();
                    info.setJobName(jobKey.getName());
                    info.setJobGroup(jobKey.getGroup());
                    info.setJobDescription(createTime);
                    info.setJobStatus(triggerState.name());
                    info.setCronExpression(cronExpression);
                    info.setCreateTime(jobDetail.getDescription());
                    list.add(info);
                }
            }
        }
        return list;
    }


    /**
     * 存定时任务
     */
    public void addJob(TaskInfo info) throws Exception{
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getCronExpression(),
                jobDescription = info.getJobDescription(),
                createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            if (checkExists(jobName, jobGroup)) {
                logger.info("===> AddJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
                throw new APIException(MyErrorConstants.PUBLIC_ERROE,String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            if (StringUtils.isBlank(cronExpression)) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE,String.format("调度运行周期信息设置不完善，cycle字段为空", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().
                    withIdentity(triggerKey).
                    withDescription(jobDescription).
                    withSchedule(schedBuilder).build();


            //Class<? extends Job> clazz = (Class<? extends Job>)Class.forName(jobName);
            String cla = info.getJobMethod();
            Class<? extends Job> clazz = (Class<? extends Job>)Class.forName(cla);
//            System.out.println(clazz);
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(createTime).usingJobData("parameter",jobDescription).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new APIException(MyErrorConstants.PUBLIC_ERROE,"类名不存在或执行表达式错误");
        }
    }


    /**
    * 修改定时任务
    */
    public void edit(TaskInfo info) throws Exception{
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getCronExpression(),
//                jobDescription = info.getJobDescription(),//不支持修改参数
                createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            if (!checkExists(jobName, jobGroup)) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE,String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(cronScheduleBuilder)
//                    .usingJobData("parameter",jobDescription)
//                    .withDescription(jobDescription)
                    .build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(createTime);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (Exception e) {
            throw new APIException(MyErrorConstants.PUBLIC_ERROE,"类名不存在或执行表达式错误");
        }
    }


    /**
     * 删除定时任务
     */
    public void delete(String jobName, String jobGroup)throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            logger.info("===> delete, triggerKey:{}", triggerKey);
        }
    }


    /**
     * 暂停定时任务
     */
    public void pause(String jobName, String jobGroup) throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.pauseTrigger(triggerKey);
            logger.info("===> Pause success, triggerKey:{}", triggerKey);
        }else{
            throw new APIException(MyErrorConstants.PUBLIC_ERROE,"不存在改定时任务");
        }
    }

    /**
     * 重新开始任务
     */
    public void resume(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
                logger.info("===> Resume success, triggerKey:{}", triggerKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证是否存在
     */
    public boolean checkExists(String jobName, String jobGroup) throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}
