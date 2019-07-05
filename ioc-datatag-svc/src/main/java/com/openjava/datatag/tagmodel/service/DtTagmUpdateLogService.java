package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTagmUpdateLog;
import com.openjava.datatag.tagmodel.query.DtTagmUpdateLogDBParam;

/**
 * 标签模型日志业务层接口
 * @author zmk
 *
 */
public interface DtTagmUpdateLogService {
	Page<DtTagmUpdateLog> query(DtTagmUpdateLogDBParam params, Pageable pageable);
	
	List<DtTagmUpdateLog> queryDataOnly(DtTagmUpdateLogDBParam params, Pageable pageable);
	
	DtTagmUpdateLog get(Long id);
	
	DtTagmUpdateLog doSave(DtTagmUpdateLog m);
	
	void doDelete(Long id);
	void doRemove(String ids);
}
