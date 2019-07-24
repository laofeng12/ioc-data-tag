package com.openjava.datatag.tagcol.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hyq
 *
 */
public class DtTagmCooLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_cooId;//协作表主键 = ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_cooId() {
		return eq_cooId;
	}
	public void setEq_cooId(Long cooId) {
		this.eq_cooId = cooId;
	}
}