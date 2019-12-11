package com.openjava.datatag.log.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.domain.DtTaggingErrorLog;
import com.openjava.datatag.log.query.DtTaggingErrorLogDBParam;

/**
 * 模型调度错误日志业务层接口
 * @author zmk
 *
 */
public interface DtTaggingErrorLogService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTaggingErrorLog> query(DtTaggingErrorLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTaggingErrorLog> queryDataOnly(DtTaggingErrorLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTaggingErrorLog get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTaggingErrorLog doSave(DtTaggingErrorLog m);

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
	 * @param taggingModelId
	 * @return
	 */
	DtTaggingErrorLog getByTaggingModelIdOrderByErrorTimeDesc(Long taggingModelId);
}
