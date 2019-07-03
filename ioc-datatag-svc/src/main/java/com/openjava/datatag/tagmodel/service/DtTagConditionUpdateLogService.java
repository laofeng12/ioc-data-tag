package com.openjava.datatag.tagmodel.service;

import java.util.List;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTagConditionUpdateLog;
import com.openjava.datatag.tagmodel.query.DtTagConditionUpdateLogDBParam;

/**
 * 条件设置表的设置日志业务层接口
 * @author zmk
 *
 */
public interface DtTagConditionUpdateLogService {
	Page<DtTagConditionUpdateLog> query(DtTagConditionUpdateLogDBParam params, Pageable pageable);
	
	List<DtTagConditionUpdateLog> queryDataOnly(DtTagConditionUpdateLogDBParam params, Pageable pageable);
	
	DtTagConditionUpdateLog get(Long id);
	
	DtTagConditionUpdateLog doSave(DtTagConditionUpdateLog m);
	
	void doDelete(Long id);
	void doRemove(String ids);
}
