package com.openjava.datatag.tagmodel.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtTagConditionDBParam extends RoDBQueryParam {
	private Long eq_tagConditionId;//条件设置主键 --主键查询
	
	private Long eq_colId;//字段表主键 = ?
	private Long eq_tagId;//标签编号 = ?
	private String eq_sourceCol;//源字段名 = ?
	private String eq_showCol;//显示字段名 = ?
	private Long eq_isDeleted;//删除标记 = ?
	
	public Long getEq_tagConditionId() {
		return eq_tagConditionId;
	}
	public void setEq_tagConditionId(Long tagConditionId) {
		this.eq_tagConditionId = tagConditionId;
	}
	
	public Long getEq_colId() {
		return eq_colId;
	}
	public void setEq_colId(Long colId) {
		this.eq_colId = colId;
	}
	public Long getEq_tagId() {
		return eq_tagId;
	}
	public void setEq_tagId(Long tagId) {
		this.eq_tagId = tagId;
	}
	public String getEq_sourceCol() {
		return eq_sourceCol;
	}
	public void setEq_sourceCol(String sourceCol) {
		this.eq_sourceCol = sourceCol;
	}
	public String getEq_showCol() {
		return eq_showCol;
	}
	public void setEq_showCol(String showCol) {
		this.eq_showCol = showCol;
	}
	public Long getEq_isDeleted() {
		return eq_isDeleted;
	}
	public void setEq_isDeleted(Long isDeleted) {
		this.eq_isDeleted = isDeleted;
	}
}