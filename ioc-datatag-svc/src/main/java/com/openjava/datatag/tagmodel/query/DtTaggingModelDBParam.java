package com.openjava.datatag.tagmodel.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtTaggingModelDBParam extends RoDBQueryParam {
	private Long eq_taggingModelId;//标签模型主键 --主键查询
	
	private String like_modelName;//模型名字 like ?
	private Long eq_runState;//运行状态:未运行/运行中/运行出错/运行结束 = ?
	private Long eq_isDeleted;//删除标记 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_startTime;//运行开始时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_startTime;//运行开始时间 >= ?
	
	public Long getEq_taggingModelId() {
		return eq_taggingModelId;
	}
	public void setEq_taggingModelId(Long taggingModelId) {
		this.eq_taggingModelId = taggingModelId;
	}
	
	public String getLike_modelName() {
		return like_modelName;
	}
	public void setLike_modelName(String modelName) {
		this.like_modelName = modelName;
	}
	public Long getEq_runState() {
		return eq_runState;
	}
	public void setEq_runState(Long runState) {
		this.eq_runState = runState;
	}
	public Long getEq_isDeleted() {
		return eq_isDeleted;
	}
	public void setEq_isDeleted(Long isDeleted) {
		this.eq_isDeleted = isDeleted;
	}
	public Date getLe_startTime() {
		return le_startTime;
	}
	public void setLe_startTime(Date startTime) {
		this.le_startTime = startTime;
	}
	public Date getGe_startTime() {
		return ge_startTime;
	}
	public void setGe_startTime(Date startTime) {
		this.ge_startTime = startTime;
	}
}