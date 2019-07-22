package com.openjava.datatag.schedule.domain;

import com.openjava.datatag.tagmodel.dto.DtTagConditionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理定时任务
 * @author zmk
 */
@Data
public class TaskInfo implements Serializable {
	private static final long serialVersionUID = -8054692082716173379L;
	@ApiModelProperty("是否新增标示，默认为1,新增")
	@Transient
	private int id = 1;

	@ApiModelProperty("任务名称")
	private String jobName;

	@ApiModelProperty("任务分组")
	private String jobGroup;

	@ApiModelProperty("任务描述")
	private String jobDescription;

	@ApiModelProperty("任务状态")
	private String jobStatus;

	@ApiModelProperty("任务表达式")
	private String cronExpression;

	@ApiModelProperty("创建时间")
	private String createTime;

	@ApiModelProperty("目标方法")
	private String jobMethod;
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
//        if (getClass() != obj.getClass()){
//            return false;
//        }
		TaskInfo taskInfo = (TaskInfo) obj;
		if (taskInfo == null) {
			return false;
		}
		if (StringUtils.isBlank(this.jobName)) {
			if (taskInfo != null){
				return false;
			}
		} else if (!this.getJobGroup().equals(taskInfo.getJobGroup()) && this.getJobName().equals(taskInfo.getJobName())){
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		List<TaskInfo> saveconditions = new ArrayList<>();
		TaskInfo d = new TaskInfo();
		TaskInfo d2 = new TaskInfo();
		d.setJobGroup("333");
		d.setJobName("111");
		d2.setJobGroup("2333");
		d2.setJobName("111");
		saveconditions.add(d);
		System.out.println(saveconditions.contains(d2));
	}
}