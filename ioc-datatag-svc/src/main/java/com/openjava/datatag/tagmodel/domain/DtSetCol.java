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
@ApiModel("字段表")
@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@Entity
@Table(name = "DT_SET_COL")
public class DtSetCol implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("字段表主键")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "COL_ID")
	private Long colId;
	
	@ApiModelProperty("标签模型编号")
	@Max(9223372036854775806L)
	@Column(name = "TAGGING_MODEL_ID")
	private Long taggingModelId;
	
	@ApiModelProperty("源字段名")
	@Length(min=1, max=32)
	@Column(name = "SOURCE_COL")
	private String sourceCol;

	@ApiModelProperty("源字段id")
	@Length(min=1, max=32)
	@Column(name = "SOURCE_COL_ID")
	private String sourceColId;


	@ApiModelProperty("源字段类型")
	@Length(min=1, max=32)
	@Column(name = "SOURCE_DATA_TYPE")
	private String sourceDataType;

	@ApiModelProperty("显示字段名")
	@Length(min=0, max=32)
	@Column(name = "SHOW_COL")
	private String showCol;
	
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
	
	@ApiModelProperty("修改用户")
	@Max(9223372036854775806L)
	@Column(name = "MODIFY_USER")
	private Long modifyUser;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	@ApiModelProperty("删除标记")
	@Max(9L)
	@Column(name = "IS_DELETED")
	private Long isDeleted;
	
	@ApiModelProperty("是否手动打标字段")
	@Max(1L)
	@Column(name = "IS_P_KEY")
	private Long isPKey;
	
	@ApiModelProperty("是否打标字段")
	@Max(1L)
	@Column(name = "IS_MARKING")
	private Long isMarking;
	
	@ApiModelProperty("是否源字段")
	@Max(9L)
	@Column(name = "IS_SOURCE")
	private Long isSource;

	@ApiModelProperty("排序")
	@Max(9223372036854775806L)
	@Column(name = "COL_SORT")
	private Long colSort;
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.colId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.colId != null) {
    		return false;
    	}
    	return true;
    }
    
}