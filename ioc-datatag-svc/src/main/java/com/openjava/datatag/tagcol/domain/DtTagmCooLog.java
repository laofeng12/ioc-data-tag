package com.openjava.datatag.tagcol.domain;

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
 * @author hyq
 *
 */
@ApiModel("tagcol")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAGM_COO_LOG")
public class DtTagmCooLog implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("日志编号")
	@Id
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("协作表主键")

	@Column(name = "COO_ID")
	private Long cooId;
	
	@ApiModelProperty("修改者")
	@Max(9223372036854775806L)
	@Column(name = "MODIFY_USER")
	private Long modifyUser;
	
	@ApiModelProperty("修改类型(修改或删除)")
	@Max(9L)
	@Column(name = "MODIFY_TYPE")
	private Long modifyType;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	@ApiModelProperty("修改者IP")
	@Length(min=0, max=48)
	@Column(name = "MODIFY_USERIP")
	private String modifyUserip;
	
	@ApiModelProperty("修改内容")
	@Length(min=0, max=-1)
	@Column(name = "MODIFY_CONTENT")
	private String modifyContent;
	
	
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