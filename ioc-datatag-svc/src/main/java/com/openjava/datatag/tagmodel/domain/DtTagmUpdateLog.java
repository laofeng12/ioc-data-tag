package com.openjava.datatag.tagmodel.domain;

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
 * @author zmk
 *
 */
@ApiModel("标签模型日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAGM_UPDATE_LOG")
public class DtTagmUpdateLog implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("日志编号")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("标签模型主键")
	@Max(9223372036854775806L)
	@Column(name = "TAGGING_MODEL_ID")
	private Long taggingModelId;
	
	@ApiModelProperty("修改者")
	@Max(9223372036854775806L)
	@Column(name = "MODIFY_USER")
	private Long modifyUser;
	
	@ApiModelProperty("修改类型(修改或删除)")
	@Column(name = "MODIFY_TYPE")
	private Long modifyType;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	@ApiModelProperty("修改者IP")
	@Length(min=0, max=16)
	@Column(name = "MODIFY_USERIP")
	private String modifyUserip;
	
	@ApiModelProperty("修改内容")
	@Column(name = "MODIFY_CONTENT")
	private String modifyContent;
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;

    
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