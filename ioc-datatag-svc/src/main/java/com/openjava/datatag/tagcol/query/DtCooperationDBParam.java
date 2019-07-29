package com.openjava.datatag.tagcol.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hyq
 *
 */
public class DtCooperationDBParam extends RoDBQueryParam {
	private Long eq_id;//协作表主键 --主键查询
	
	private Long eq_createUser;//(不传时默认当前用户)发起者 = ?
	private Long eq_taggmId;//标签模型主键 = ?

	public Long getEq_taggmId() {
		return eq_taggmId;
	}

	public void setEq_taggmId(Long eq_taggmId) {
		this.eq_taggmId = eq_taggmId;
	}

	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_createUser() {
		return eq_createUser;
	}
	public void setEq_createUser(Long createUser) {
		this.eq_createUser = createUser;
	}


}