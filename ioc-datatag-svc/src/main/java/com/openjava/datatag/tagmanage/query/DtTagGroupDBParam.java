package com.openjava.datatag.tagmanage.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author lch
 *
 */
public class DtTagGroupDBParam extends RoDBQueryParam {
	private Long eq_id;//标签组编号 --主键查询
	
	private String keyword;//关键字 like ?
	private String sql_key;//通过关键字like 简介和标签组名
	private Long eq_isShare;//是否共享 = ?
	//private String like_synopsis;//标签组简介 like ?
	private Long eq_createUser;//创建者 = ?
	//private String eq_modifyUser;//修改者名 = ?
	private Long eq_isDeleted;//删除标记 = ?

	public String getSql_key() {
		return sql_key;
	}
	public void setSql_key(String sql_key) {
		this.sql_key = sql_key;
	}



	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String tagsName) {
		this.keyword = tagsName;
	}
	public Long getEq_isShare() {
		return eq_isShare;
	}
	public void setEq_isShare(Long isShare) {
		this.eq_isShare = isShare;
	}
	public Long getEq_createUser() {
		return eq_createUser;
	}
	public void setEq_createUser(Long user) {
		this.eq_createUser = user;
	}
//	public String getEq_modifyUser() {
//		return eq_modifyUser;
//	}
//	public void setEq_modifyUser(String modifyUser) {
//		this.eq_modifyUser = modifyUser;
//	}
	public Long getEq_isDeleted() {
		return eq_isDeleted;
	}
	public void setEq_isDeleted(Long isDeleted) {
		this.eq_isDeleted = isDeleted;
	}
}