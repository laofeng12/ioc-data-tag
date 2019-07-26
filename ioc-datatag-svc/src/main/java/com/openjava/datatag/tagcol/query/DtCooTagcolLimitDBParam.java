package com.openjava.datatag.tagcol.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hyq
 *
 */
public class DtCooTagcolLimitDBParam extends RoDBQueryParam {
	private Long eq_id;//协作打标列限制表ID --主键查询
	
	private String like_tagColName;//协作可打标字段名 like ?

	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public String getLike_tagColName() {
		return like_tagColName;
	}
	public void setLike_tagColName(String tagColName) {
		this.like_tagColName = tagColName;
	}

}