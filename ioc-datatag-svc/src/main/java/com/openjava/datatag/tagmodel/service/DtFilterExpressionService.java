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

	/**
	 *
	 * @param id
	 * @return
	 */
	DtFilterExpression get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtFilterExpression doSave(DtFilterExpression m);

	/**
	 *
	 * @param id
	 */
	void doDelete(Long id);

	/**
	 *
	 * @param ids
	 */
	void doRemove(String ids);

	/**
	 *
	 * @param tagConditionId
	 * @return
	 */
	List<DtFilterExpression> findByTagConditionId(Long tagConditionId);

	/**
	 *
	 * @param tagConditionId
	 */
	void doRemoveByTagConditionId(Long tagConditionId);
}
