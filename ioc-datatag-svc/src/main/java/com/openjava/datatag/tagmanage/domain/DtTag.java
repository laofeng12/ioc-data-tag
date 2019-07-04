package com.openjava.datatag.tagmanage.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;

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
@ApiModel("DT_TAG")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAG")
public class DtTag implements Persistable<Long>,Serializable {

	@ApiModelProperty("标签编号")
	@Id
	@Column(name = "ID")
	private Long id;

	@ApiModelProperty("标签组编号")
	@Max(9223372036854775806L)
	@Column(name = "TAGS_ID")
	private Long tagsId;

	@ApiModelProperty("父标签编号")
	@Max(9223372036854775806L)
	@Column(name = "PREA_TAG_ID")
	private Long preaTagId;

	@ApiModelProperty("标签名")
	@Length(min=0, max=32)
	@Column(name = "TAG_NAME")
	private String tagName;

	@ApiModelProperty("标签说明")
	@Length(min=0, max=2000)
	@Column(name = "SYNOPSIS")
	private String synopsis;

	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	@ApiModelProperty("删除标记")
	@Max(1L)
	@Column(name = "IS_DELETED")
	private Long isDeleted;

	@ApiModelProperty("层级")
	@Max(1L)
	@Column(name = "LVL")
	private Long lvl;


	@ApiModelProperty("是否新增")
	@Transient
	private Boolean isNew;

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