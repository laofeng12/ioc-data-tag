package com.openjava.datatag.tagmanage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author lch
 *
 */
@ApiModel("DT_TAG")
@Entity
@Table(name = "DT_TAG")
public class DtTag implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("标签编号")
	private Long id;
	@ApiModelProperty("标签组编号")
	private Long tagsId;
	@ApiModelProperty("父标签编号")
	private Long preaTagId;
	@ApiModelProperty("标签名")
	private String tagName;
	@ApiModelProperty("标签说明")
	private String synopsis;
	@ApiModelProperty("创建时间")
	private Date createTime;
	@ApiModelProperty("修改时间")
	private Date modifyTime;
	@ApiModelProperty("删除标记")
	private Long isDeleted;
	@ApiModelProperty("层级")
	private Long lvl;
	
	@ApiModelProperty("是否新增")
    private Boolean isNew;
	
//	@Transient
//    @JsonIgnore
//    @Override
//    public Long getId() {
//        return this.id;
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
    
    @Transient
    public Boolean getIsNew() {
		return isNew;
	}
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	@Column(name = "TAGS_ID")
	public Long getTagsId() {
		return tagsId;
	}
	public void setTagsId(Long tagsId) {
		this.tagsId = tagsId;
	}
	

	@Column(name = "PREA_TAG_ID")
	public Long getPreaTagId() {
		return preaTagId;
	}
	public void setPreaTagId(Long preaTagId) {
		this.preaTagId = preaTagId;
	}
	

	@Column(name = "TAG_NAME")
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	

	@Column(name = "SYNOPSIS")
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	

	@Column(name = "IS_DELETED")
	public Long getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
	

	@Column(name = "LVL")
	public Long getLvl() {
		return lvl;
	}
	public void setLvl(Long lvl) {
		this.lvl = lvl;
	}
	
}