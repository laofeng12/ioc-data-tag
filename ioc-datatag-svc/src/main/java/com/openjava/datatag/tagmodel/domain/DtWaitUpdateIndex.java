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
@ApiModel("更新索引表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_WAIT_UPDATE_INDEX")
public class DtWaitUpdateIndex implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("模型运行结果表ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "WAIT_UPDATE_INDEX_ID")
	private Long waitUpdateIndexId;
	
	@ApiModelProperty("标签模型主键")
	@Column(name = "TAGGING_MODEL_ID")
	private Long taggingModelId;

	@ApiModelProperty("标签模型主键名称")
	@Column(name = "MODEL_KEY_COL_NAME")
	private String modelKeyColName;

	@ApiModelProperty("表名")
	@Column(name = "TABLE_NAME")
	private String tableName;
	
	@ApiModelProperty("索引更新运行状态（0未开始1已开始）")
	@Column(name = "RUN_STATE")
	private Long runState;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.waitUpdateIndexId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.waitUpdateIndexId != null) {
    		return false;
    	}
    	return true;
    }
    
}