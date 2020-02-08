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
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author hyq
 *
 */

@Data
public class DtCooperationSetCol {
	
	@ApiModelProperty("字段表主键Id")
	private Long colId;
	
	@ApiModelProperty("标签模型编号")
	private Long taggingModelId;
	
	@ApiModelProperty("源字段名")
	private String sourceCol;

	@ApiModelProperty("源字段类型")
	private String sourceDataType;

	@ApiModelProperty("显示字段名")
	private String showCol;

	@ApiModelProperty("字段中文注释")
	private String comment;

	@ApiModelProperty("创建用户")
	private Long createUser;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createTime;
	
	@ApiModelProperty("修改用户")
	private Long modifyUser;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date modifyTime;
	
	@ApiModelProperty("删除标记")
	private Long isDeleted;
	
	@ApiModelProperty("是否手动打标字段")
	private Long isPKey;
	
	@ApiModelProperty("是否打标字段")
	private Long isMarking;
	
	@ApiModelProperty("是否源字段")
	private Long isSource;
	@ApiModelProperty("协作表主键Id")
	private Long id;
	@ApiModelProperty("协作字段表主键Id")
	private Long cooFieldId;
	@ApiModelProperty("是否协作打标字段")
	private Long isCooField;

	@ApiModelProperty("协作用户Id")
	private Long cooUser;
	@ApiModelProperty("模型的字段Id")
	private Long tagColId;

	@ApiModelProperty("协作用户名")
	private String cooUserName;
	@ApiModelProperty("规定使用的标签组ID")
	private Long useTagGroup;
	@ApiModelProperty("创建用户名")
	private String createUserName;
	@ApiModelProperty("修改用户名")
	private String modifyUserName;

    
}