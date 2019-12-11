package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.query.DtTagConditionDBParam;
import org.springframework.data.repository.query.Param;

/**
 * 条件设置表业务层接口
 * @author zmk
 *
 */
public interface DtTagConditionService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTagCondition> query(DtTagConditionDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTagCondition> queryDataOnly(DtTagConditionDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTagCondition get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTagCondition doSave(DtTagCondition m);

	/**
	 *
	 * @param id
	 * @throws Exception
	 */
	void doDelete(Long id)throws Exception;

	/**
	 *
	 * @param ids
	 * @throws Exception
	 */
	void doRemove(String ids)throws Exception;

	/**
	 *
	 * @param colId
	 * @return
	 */
	List<DtTagCondition> findByColId(Long colId);

	/**
	 *
	 * @param colIds
	 * @return
	 */
	List<DtTagCondition> findByColIds(List<Long> colIds);

	/**
	 * 根据模型id和标签id获取条件设置
	 * @param taggingModelId
	 * @param tagId
	 * @return
	 */
	List<DtTagCondition> findByTaggingModelIdAndColId(Long taggingModelId ,Long tagId);

	/**
	 *
	 * @param tagIds
	 * @return
	 */
	List<DtTagCondition> findByTagIds(List<Long> tagIds);
}
