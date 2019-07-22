package org.openjava.boot;

import com.openjava.datatag.component.SchedulejobCompent;
import com.openjava.datatag.schedule.domain.TaskInfo;
import com.openjava.datatag.schedule.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ljdp.common.spring.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class DatatagRunner implements ApplicationRunner {
	private Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private SpringContext springContext;
	@Resource
	private TaskService taskService;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	SchedulejobCompent schedulejobCompent;

	public void run(ApplicationArguments args) throws Exception {
		TaskInfo taskInfo = new TaskInfo();
		taskInfo.setJobGroup(schedulejobCompent.getJobGroup());
		taskInfo.setJobName(schedulejobCompent.getJobName());
		taskInfo.setCronExpression(schedulejobCompent.getQueue());
		taskInfo.setJobMethod(schedulejobCompent.getJobMethod());
		taskInfo.setId(1);
		taskService.addJob(taskInfo);
		logger.info("定时任务：新建模型扫描任务已启动");
		logger.info("数据标签与画像组件启动成功");
	}

}
