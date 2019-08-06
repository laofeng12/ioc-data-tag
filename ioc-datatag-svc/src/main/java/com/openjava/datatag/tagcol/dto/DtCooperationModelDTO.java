package com.openjava.datatag.tagcol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author hyq
 *
 */

@Data
public class DtCooperationModelDTO {
	@ApiModelProperty("标签模型主键")
	private Long taggingModelId;
	@ApiModelProperty("模型名字")
	private String modelName;
	@ApiModelProperty("模型简介")
	private String modelDesc;

	@ApiModelProperty("打标目标表id")
	private Long resourceId;
	
	@ApiModelProperty("打标源表名称")
	private String resourceName;

	@ApiModelProperty("打标目的表名")
	private String dataTableName;

	@ApiModelProperty("声明的主键")
	private String pkey;

	@ApiModelProperty("创建用户Id")
	private Long createUser;

	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createTime;
	
	@ApiModelProperty("修改用户Id")
	private Long modifyUser;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date modifyTime;
	
	@ApiModelProperty("运行开始时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date startTime;
	
	@ApiModelProperty("调度运行周期-cron 表达式")
	private String cycle;

	@ApiModelProperty("调度运行周期- 周期枚举 DT_MODEL_DISPATCH")
	private Long cycleEnum;
	
	@ApiModelProperty("运行状态:未运行/运行中/运行出错/运行结束")
	private Long runState;
	
	@ApiModelProperty("删除标记")
	private Long isDeleted;

	@ApiModelProperty("创建用户名")
	private String createUserName;
	@ApiModelProperty("修改用户名")
	private String modifyUserName;
	@ApiModelProperty("协作用户Id")
	private Long cooUser;
	@ApiModelProperty("协作用户名")
	private String cooUserName;

}