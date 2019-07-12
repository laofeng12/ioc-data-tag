package com.openjava.datatag.log.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author lch
 *
 */
public class DtTaggChooseLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_copiedTagg;//被拷贝的标签组编号 = ?
	private Long eq_copyTagg;//拷贝的标签组编号 = ?
	private Long eq_chooseUser;//选用者 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_chooseTime;//选用时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_chooseTime;//选用时间 >= ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_copiedTagg() {
		return eq_copiedTagg;
	}
	public void setEq_copiedTagg(Long copiedTagg) {
		this.eq_copiedTagg = copiedTagg;
	}
	public Long getEq_copyTagg() {
		return eq_copyTagg;
	}
	public void setEq_copyTagg(Long copyTagg) {
		this.eq_copyTagg = copyTagg;
	}
	public Long getEq_chooseUser() {
		return eq_chooseUser;
	}
	public void setEq_chooseUser(Long chooseUser) {
		this.eq_chooseUser = chooseUser;
	}
	public Date getLe_chooseTime() {
		return le_chooseTime;
	}
	public void setLe_chooseTime(Date chooseTime) {
		this.le_chooseTime = chooseTime;
	}
	public Date getGe_chooseTime() {
		return ge_chooseTime;
	}
	public void setGe_chooseTime(Date chooseTime) {
		this.ge_chooseTime = chooseTime;
	}
}