package com.openjava.datatag.dowload.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DownloadQueueDBParam extends RoDBQueryParam {
	private Long eq_id;//ID --主键查询
	
	private String eq_btype;//业务类型(标签与画像、数据集、数据碰撞等) = ?
	private String eq_bid;//业务ID = ?
	private Long eq_state;//状态(下载中、下载失败) = ?
	private String like_bname;//业务名称 like ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public String getEq_btype() {
		return eq_btype;
	}
	public void setEq_btype(String btype) {
		this.eq_btype = btype;
	}
	public String getEq_bid() {
		return eq_bid;
	}
	public void setEq_bid(String bid) {
		this.eq_bid = bid;
	}
	public Long getEq_state() {
		return eq_state;
	}
	public void setEq_state(Long state) {
		this.eq_state = state;
	}
	public String getLike_bname() {
		return like_bname;
	}
	public void setLike_bname(String bname) {
		this.like_bname = bname;
	}
}