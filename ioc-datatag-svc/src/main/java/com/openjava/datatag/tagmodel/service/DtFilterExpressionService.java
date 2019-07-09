package com.openjava.datatag.tagmodel.service;

import com.openjava.datatag.tagmodel.domain.DtFilterExpression;

import java.util.List;

/**
 * 规制表达式业务层接口
 * @author zmk
 *
 */
public interface DtFilterExpressionService {
//	Page<DtFilterExpression> query(DtFilterExpressionDBParam params, Pageable pageable);
//
//	List<DtFilterExpression> queryDataOnly(DtFilterExpressionDBParam params, Pageable pageable);
	
	DtFilterExpression get(Long id);
	
	DtFilterExpression doSave(DtFilterExpression m);
	
	void doDelete(Long id);
	void doRemove(String ids);

	List<DtFilterExpression> findByTagConditionId(Long tagConditionId);

	void doRemoveByTagConditionId(Long tagConditionId);
}
