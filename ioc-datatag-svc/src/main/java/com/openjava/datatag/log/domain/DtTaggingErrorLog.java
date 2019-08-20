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
 * @author zmk
 *
 */
@ApiModel("模型调度错误日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAGGING_ERROR_LOG")
public class DtTaggingErrorLog implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("标签模型主键")
	@Column(name = "TAGGING_MODEL_ID")
	private Long taggingModelId;
	
	@ApiModelProperty("报错信息")
	@Column(name = "ERROR_INFO")
	private byte[] errorInfo;
	
	@ApiModelProperty("报错时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ERROR_TIME")
	private Date errorTime;
	
	
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