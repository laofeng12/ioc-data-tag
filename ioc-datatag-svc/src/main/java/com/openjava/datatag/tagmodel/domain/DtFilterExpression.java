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
@ApiModel("规制表达式")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DT_FILTER_EXPRESSION")
public class DtFilterExpression implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("规制表达式表ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "FILTER_EXPRESSION_ID")
	private Long filterExpressionID;
	
	@ApiModelProperty("条件设置主键")
	@Max(0L)
	@Column(name = "TAG_CONDITION_ID")
	private Long tagConditionId;
	
	@ApiModelProperty("符号")
	@Length(min=0, max=32)
	@Column(name = "SYMBOL")
	private String symbol;
	
	@ApiModelProperty("条件值")
	@Length(min=0, max=4000)
	@Column(name = "THE_VALUES")
	private String theValues;
	
	@ApiModelProperty("值类型")
	@Length(min=0, max=32)
	@Column(name = "VALUES_TYPE")
	private String valuesType;
	
	@ApiModelProperty("是否连接符")
	@Max(1L)
	@Column(name = "IS_CONNECT_SYMBOL")
	private Long isConnectSymbol;
	
	@ApiModelProperty("排序")
	@Max(9999L)
	@Column(name = "SORT")
	private Integer sort;
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.filterExpressionID;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.filterExpressionID != null) {
    		return false;
    	}
    	return true;
    }
    
}