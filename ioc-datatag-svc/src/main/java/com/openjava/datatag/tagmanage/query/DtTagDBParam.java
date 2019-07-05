package com.openjava.datatag.tagmanage.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author lch
 *
 */
public class DtTagDBParam extends RoDBQueryParam {
	private Long eq_id;//标签编号 --主键查询
	
	private Long eq_tagsId;//标签组编号 = ?
	private Long eq_preaTagId;//父标签编号 = ?
	private String like_tagName;//标签名 like ?
	private Long eq_lvl;//层级 = ?
	private Long eq_isDeleted;//删除标记 = ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_tagsId() {
		return eq_tagsId;
	}
	public void setEq_tagsId(Long tagsId) {
		this.eq_tagsId = tagsId;
	}
	public Long getEq_preaTagId() {
		return eq_preaTagId;
	}
	public void setEq_preaTagId(Long preaTagId) {
		this.eq_preaTagId = preaTagId;
	}
	public String getLike_tagName() {
		return like_tagName;
	}
	public void setLike_tagName(String tagName) {
		this.like_tagName = tagName;
	}
	public Long getEq_lvl() {
		return eq_lvl;
	}
	public void setEq_lvl(Long lvl) {
		this.eq_lvl = lvl;
	}
	public Long getEq_isDeleted() {
		return eq_isDeleted;
	}
	public void setEq_isDeleted(Long isDeleted) {
		this.eq_isDeleted = isDeleted;
	}
}