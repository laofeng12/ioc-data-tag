package com.openjava.datatag.schedule.service;

import com.openjava.datatag.schedule.domain.TaskInfo;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author zmk
 */
public interface TaskService {
    /**
     * 所有任务列表
     */
    List<TaskInfo> list() throws Exception;
    /**
     * 存定时任务
     */
    void addJob(TaskInfo info) throws Exception;
    /**
     * 修改定时任务
     */
    void edit(TaskInfo info) throws Exception;
    /**
     * 删除定时任务
     */
    void delete(String jobName, String jobGroup)throws Exception;
    /**
     * 暂停定时任务
     */
    void pause(String jobName, String jobGroup) throws Exception;
    /**
     * 重新开始任务
     */
    void resume(String jobName, String jobGroup);
    /**
     * 验证是否存在
     */
    boolean checkExists(String jobName, String jobGroup) throws SchedulerException;
}
