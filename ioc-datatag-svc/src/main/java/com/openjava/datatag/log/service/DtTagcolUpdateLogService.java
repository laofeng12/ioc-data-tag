package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.query.DtTagcolUpdateLogDBParam;

/**
 * 字段表日志业务层接口
 * @author zmk
 *
 */
public interface DtTagcolUpdateLogService {
	Page<DtTagcolUpdateLog> query(DtTagcolUpdateLogDBParam params, Pageable pageable);
	
	List<DtTagcolUpdateLog> queryDataOnly(DtTagcolUpdateLogDBParam params, Pageable pageable);
	
	DtTagcolUpdateLog get(Long id);
	
	DtTagcolUpdateLog doSave(DtTagcolUpdateLog m);

	DtTagcolUpdateLog loggingUpdate(String content,DtSetCol db, String ip);

	DtTagcolUpdateLog loggingNew(String content,DtSetCol body,String ip);

	DtTagcolUpdateLog loggingDelete(DtSetCol db,String ip);

	void doDelete(Long id);
	void doRemove(String ids);
}
