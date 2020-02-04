package com.openjava.datatag.log.domain;

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
@ApiModel("DT_TAGG_CHOOSE_LOG")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAGG_CHOOSE_LOG")
public class DtTaggChooseLog implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("日志编号")
	@Id
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("被拷贝的标签组编号")
	@Max(9223372036854775806L)
	@Column(name = "COPIED_TAGG")
	private Long copiedTagg;
	
	@ApiModelProperty("拷贝的标签组编号")
	@Max(9223372036854775806L)
	@Column(name = "COPY_TAGG")
	private Long copyTagg;
	
	@ApiModelProperty("选用者")
	@Max(9223372036854775806L)
	@Column(name = "CHOOSE_USER")
	private Long chooseUser;

	@ApiModelProperty("选用者姓名")
	@Column(name = "CHOOSE_USER_NAME")
	private String chooseUserName;

	@ApiModelProperty("选用时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHOOSE_TIME")
	private Date chooseTime;
	
	@ApiModelProperty("选用者IP")
	@Length(min=0, max=48)
	@Column(name = "CHOOSER_IP")
	private String chooserIp;

	@ApiModelProperty("选用部门id")
	@Column(name = "CHOOSE_ORG_ID")
	private String chooseOrgid;

	@ApiModelProperty("选用部门名称")
	@Column(name = "CHOOSE_ORG_NAME")
	private String chooseOrgNmae;
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.id;
	}
    
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