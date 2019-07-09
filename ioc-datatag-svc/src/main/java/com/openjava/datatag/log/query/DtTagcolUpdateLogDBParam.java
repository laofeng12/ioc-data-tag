package com.openjava.datatag.log.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtTagcolUpdateLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_colId;//字段表主键 = ?
	private String eq_modifyType;//修改类型(修改或删除) = ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_colId() {
		return eq_colId;
	}
	public void setEq_colId(Long colId) {
		this.eq_colId = colId;
	}
	public String getEq_modifyType() {
		return eq_modifyType;
	}
	public void setEq_modifyType(String modifyType) {
		this.eq_modifyType = modifyType;
	}
}