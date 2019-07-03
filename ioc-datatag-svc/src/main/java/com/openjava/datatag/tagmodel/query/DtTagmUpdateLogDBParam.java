package com.openjava.datatag.tagmodel.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtTagmUpdateLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_taggingModelId;//标签模型主键 = ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_taggingModelId() {
		return eq_taggingModelId;
	}
	public void setEq_taggingModelId(Long taggingModelId) {
		this.eq_taggingModelId = taggingModelId;
	}
}