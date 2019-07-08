package com.openjava.datatag.log.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author lch
 *
 */
public class DtTagUpdateLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_tagId;//标签编号 = ?
	private Long eq_modifyUser;//修改者 = ?
	private String eq_modifyType;//修改类型(修改或删除) = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_modifyTime;//修改时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_modifyTime;//修改时间 >= ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_tagId() {
		return eq_tagId;
	}
	public void setEq_tagId(Long tagId) {
		this.eq_tagId = tagId;
	}
	public Long getEq_modifyUser() {
		return eq_modifyUser;
	}
	public void setEq_modifyUser(Long modifyUser) {
		this.eq_modifyUser = modifyUser;
	}
	public String getEq_modifyType() {
		return eq_modifyType;
	}
	public void setEq_modifyType(String modifyType) {
		this.eq_modifyType = modifyType;
	}
	public Date getLe_modifyTime() {
		return le_modifyTime;
	}
	public void setLe_modifyTime(Date modifyTime) {
		this.le_modifyTime = modifyTime;
	}
	public Date getGe_modifyTime() {
		return ge_modifyTime;
	}
	public void setGe_modifyTime(Date modifyTime) {
		this.ge_modifyTime = modifyTime;
	}
}