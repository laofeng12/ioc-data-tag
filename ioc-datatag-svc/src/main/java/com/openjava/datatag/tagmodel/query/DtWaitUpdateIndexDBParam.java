package com.openjava.datatag.tagmodel.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtWaitUpdateIndexDBParam extends RoDBQueryParam {
	private Long eq_tmRunResultId;//模型运行结果表ID --主键查询
	
	
	public Long getEq_tmRunResultId() {
		return eq_tmRunResultId;
	}
	public void setEq_tmRunResultId(Long tmRunResultId) {
		this.eq_tmRunResultId = tmRunResultId;
	}
	
}