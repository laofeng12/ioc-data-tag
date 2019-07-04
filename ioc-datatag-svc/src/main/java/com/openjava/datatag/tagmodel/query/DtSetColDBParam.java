package com.openjava.datatag.tagmodel.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtSetColDBParam extends RoDBQueryParam {
	private Long eq_colId;//字段表主键 --主键查询
	
	private Long eq_taggingModelId;//标签模型编号 = ?
	private String eq_sourceCol;//源字段名 = ?
	private String eq_showCol;//显示字段名 = ?
	private Long eq_isMlanual;//是否手动打标字段 = ?
	private Long eq_isMarking;//是否打标字段 = ?
	private Long eq_isSource;//是否源字段 = ?
	
	public Long getEq_colId() {
		return eq_colId;
	}
	public void setEq_colId(Long colId) {
		this.eq_colId = colId;
	}
	
	public Long getEq_taggingModelId() {
		return eq_taggingModelId;
	}
	public void setEq_taggingModelId(Long taggingModelId) {
		this.eq_taggingModelId = taggingModelId;
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
	public Long getEq_isMlanual() {
		return eq_isMlanual;
	}
	public void setEq_isMlanual(Long isMlanual) {
		this.eq_isMlanual = isMlanual;
	}
	public Long getEq_isMarking() {
		return eq_isMarking;
	}
	public void setEq_isMarking(Long isMarking) {
		this.eq_isMarking = isMarking;
	}
	public Long getEq_isSource() {
		return eq_isSource;
	}
	public void setEq_isSource(Long isSource) {
		this.eq_isSource = isSource;
	}
}