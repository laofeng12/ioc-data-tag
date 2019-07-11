package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.query.DtTagConditionDBParam;

/**
 * 条件设置表业务层接口
 * @author zmk
 *
 */
public interface DtTagConditionService {
	Page<DtTagCondition> query(DtTagConditionDBParam params, Pageable pageable);
	
	List<DtTagCondition> queryDataOnly(DtTagConditionDBParam params, Pageable pageable);
	
	DtTagCondition get(Long id);
	
	DtTagCondition doSave(DtTagCondition m);
	
	void doDelete(Long id)throws Exception;
	void doRemove(String ids)throws Exception;
	List<DtTagCondition> findByColId(Long colId);
	List<DtTagCondition> findByColIds(List<Long> colIds);
}
