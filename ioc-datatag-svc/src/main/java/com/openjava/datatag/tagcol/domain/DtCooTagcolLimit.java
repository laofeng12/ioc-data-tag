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
@Table(name = "DT_COO_TAGCOL_LIMIT")
public class DtCooTagcolLimit implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("协作打标列限制表ID")
	@Id
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("协作表主键")

	@Column(name = "COO_ID")
	private Long cooId;
	
	@ApiModelProperty("协作可打标字段名")
	@Length(min=0, max=32)
	@Column(name = "TAG_COL_NAME")
	private String tagColName;
	
	@ApiModelProperty("规定使用的标签组")

	@Column(name = "USE_TAG_GROUP")
	private Long useTagGroup;
	@ApiModelProperty("模型的字段Id")
	@Column(name = "TAG_COL_ID")
	private Long tagColId;

	@ApiModelProperty("状态")
	@Column(name = "STATE")
	private Long state;


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