package com.openjava.datatag.log.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DtTaggingErrorLogDBParam extends RoDBQueryParam {
	private Long eq_id;//主键 --主键查询
	
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
}