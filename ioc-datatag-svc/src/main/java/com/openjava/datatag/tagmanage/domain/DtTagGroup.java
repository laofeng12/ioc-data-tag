package com.openjava.datatag.tagmanage.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;

import com.openjava.datatag.tagmanage.dto.BaseMessageDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Persistable;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author lch
 *
 */
@ApiModel("DT_TAG_GROUP")
@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@Entity
@Table(name = "DT_TAG_GROUP")
public class DtTagGroup extends BaseMessageDTO implements Persistable<Long>,Serializable {

	@ApiModelProperty("标签组编号")
	@Id
	@Column(name = "ID")
	private Long id;//标签组编号

	@ApiModelProperty("标签组名")
	@Length(min=0, max=64)
	@Column(name = "TAGS_NAME")
	private String tagsName;//标签组名

	@ApiModelProperty("是否共享")
	@Max(1)
	@Column(name = "IS_SHARE")
	private Long isShare;//是否共享

	@ApiModelProperty("标签组简介")
	@Length(min=0, max=2000)
	@Column(name = "SYNOPSIS")
	private String synopsis;//标签组简介

	@ApiModelProperty("使用热度")
	@Max(9223372036854775806L)
	@Column(name = "POPULARITY")
	private Long popularity;//使用热度

	@ApiModelProperty("创建者")
	@Max(9223372036854775806L)
	@Column(name = "CREATE_USER")
	private Long createUser;//创建者

	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Column(name = "CREATE_TIME")
	private Date createTime;//创建时间

	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;//修改时间

	@ApiModelProperty("删除标记")
	@Max(1L)
	@Column(name = "IS_DELETED")
	private Long isDeleted;//删除标记


	@ApiModelProperty("是否新增")
	@Transient
	private Boolean isNew;//是否新增

	@ApiModelProperty("热度等级-从0~4")
	@Transient
	private Long popularityLevel;//热度等级-从0~4

	@ApiModelProperty("热度百分比")
	@Transient
	private Long percentage;//热度百分比



//	@Transient
//	@JsonIgnore
//	@Override
//	public Long getId() {
//		return this.id;
//	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isNew() {
		if(isNew != null) {
			return isNew;
		}
		if(this.id != null) {
			return false;
		}
		return true;
	}

}