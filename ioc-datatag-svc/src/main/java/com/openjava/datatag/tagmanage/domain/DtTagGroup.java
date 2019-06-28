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
@ApiModel("DT_TAG_GROUP")
@Entity
@Table(name = "DT_TAG_GROUP")
public class DtTagGroup implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("标签组编号")
	private Long id;
	@ApiModelProperty("标签组名")
	private String tagsName;
	@ApiModelProperty("是否共享")
	private Long isShare;
	@ApiModelProperty("标签组简介")
	private String synopsis;
	@ApiModelProperty("使用热度")
	private Long popularity;
	@ApiModelProperty("创建者")
	private Long createUser;
	@ApiModelProperty("创建时间")
	private Date createTime;
//	@ApiModelProperty("修改者名")
//	private String modifyUser;
	@ApiModelProperty("修改时间")
	private Date modifyTime;
	@ApiModelProperty("删除标记")
	private Long isDeleted;
	
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
	

	@Column(name = "TAGS_NAME")
	public String getTagsName() {
		return tagsName;
	}
	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}
	

	@Column(name = "IS_SHARE")
	public Long getIsShare() {
		return isShare;
	}
	public void setIsShare(Long isShare) {
		this.isShare = isShare;
	}
	

	@Column(name = "SYNOPSIS")
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	

	@Column(name = "POPULARITY")
	public Long getPopularity() {
		return popularity;
	}
	public void setPopularity(Long popularity) {
		this.popularity = popularity;
	}
	

	@Column(name = "CREATE_USER")
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
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
	

//	@Column(name = "MODIFY_USER")
//	public String getModifyUser() {
//		return modifyUser;
//	}
//	public void setModifyUser(String modifyUser) {
//		this.modifyUser = modifyUser;
//	}
	
	
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
	
}