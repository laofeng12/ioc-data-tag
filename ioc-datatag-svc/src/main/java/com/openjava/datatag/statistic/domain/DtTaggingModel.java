package com.openjava.datatag.statistic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author maliang
 *
 */
@ApiModel("标签模型")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_TAGGING_MODEL")
public class DtTaggingModel implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("标签模型主键")
	@Id
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("模型名字")
	@Length(min=0, max=32)
	@Column(name = "MODEL_NAME")
	private String modelName;
	
	@ApiModelProperty("打标目标表")
	@Length(min=0, max=200)
	@Column(name = "TAGGING_TABLE")
	private String taggingTable;
	
	@ApiModelProperty("打标临时表/模型表")
	@Length(min=0, max=200)
	@Column(name = "TAGGING_MODEL_TABLE")
	private String taggingModelTable;
	
	@ApiModelProperty("声明的主键")
	@Length(min=0, max=32)
	@Column(name = "P_KEY")
	private String pKey;
	
	@ApiModelProperty("创建用户")
	@Max(9223372036854775806L)
	@Column(name = "CREATE_USER")
	private Long createUser;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("修改用户名")
	@Length(min=0, max=32)
	@Column(name = "MODIFY_USER")
	private String modifyUser;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	@ApiModelProperty("运行开始时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	private Date startTime;
	
	@ApiModelProperty("调度运行周期")
	@Length(min=0, max=32)
	@Column(name = "CYCLE")
	private String cycle;
	
	@ApiModelProperty("运行状态:未运行/运行中/运行出错/运行结束")
	@Max(9L)
	@Column(name = "RUN_STATE")
	private Long runState;
	
	@ApiModelProperty("删除标记")
	@Max(9L)
	@Column(name = "IS_DELETED")
	private Long isDeleted;
	
	
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