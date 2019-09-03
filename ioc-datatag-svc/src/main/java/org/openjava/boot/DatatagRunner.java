package org.openjava.boot;

import com.openjava.datatag.component.SchedulejobCompent;
import com.openjava.datatag.component.SearchjobCompent;
import com.openjava.datatag.schedule.domain.TaskInfo;
import com.openjava.datatag.schedule.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ljdp.common.spring.SpringContext;
import org.ljdp.support.web.loader.LjdpWebLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class DatatagRunner implements ApplicationRunner {
	private Logger logger = LogManager.getLogger(getClass());
	@Value("${debug}")
	private boolean environmental;
	@Autowired
	private SpringContext springContext;
	@Resource
	private TaskService taskService;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	SchedulejobCompent schedulejobCompent;
	@Autowired
	SearchjobCompent searchjobCompent;

	public void run(ApplicationArguments args) throws Exception {
//		System.out.println(environmental);
		/*if (!environmental){
			//扫描待执行模型，并运行模型
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.setJobGroup(schedulejobCompent.getJobGroup());
			taskInfo.setJobName(schedulejobCompent.getJobName());
			taskInfo.setCronExpression(schedulejobCompent.getQueue());
			taskInfo.setJobMethod(schedulejobCompent.getJobMethod());
			taskInfo.setId(1);
			taskService.addJob(taskInfo);
			//扫描运行完的模型，并更新到中间表
			taskInfo.setJobGroup(searchjobCompent.getJobGroup());
			taskInfo.setJobName(searchjobCompent.getJobName());
			taskInfo.setCronExpression(searchjobCompent.getQueue());
			taskInfo.setJobMethod(searchjobCompent.getJobMethod());
			taskInfo.setId(1);
			taskService.addJob(taskInfo);
			logger.info("定时任务：模型扫描任务已启动");
			logger.info("数据标签与画像组件启动成功");
		}else{
			logger.info("开发环境，不跑定时任务。需要时请改DatatagRunner的environmental参数");
		}*/
//        LjdpWebLoader.contextInitialized();
//        System.out.println("LJDP启动成功");
	}

}
