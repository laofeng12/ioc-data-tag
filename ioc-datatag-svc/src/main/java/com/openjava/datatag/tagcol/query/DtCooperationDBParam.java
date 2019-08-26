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
	private String keyWord;//(非必填)关键字查询 ?
    private Long runState;//协作完成状态:进行中/已完成?
	private Long eq_id;//协作表主键 --主键查询
	private Long eq_createUser;//(不传时默认当前用户)发起者 = ?
	private Long eq_taggmId;//标签模型主键 = ?
	private Long eq_cooUser;//(不传时默认当前用户)协作用户 = ?
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

	public Long getEq_cooUser() {
		return eq_cooUser;
	}

	public void setEq_cooUser(Long eq_cooUser) {
		this.eq_cooUser = eq_cooUser;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

    public Long getRunState() {
        return runState;
    }

    public void setRunState(Long runState) {
        this.runState = runState;
    }
}