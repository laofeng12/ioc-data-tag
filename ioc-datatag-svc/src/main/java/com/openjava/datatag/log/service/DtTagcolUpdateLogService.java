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
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTagcolUpdateLog> query(DtTagcolUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTagcolUpdateLog> queryDataOnly(DtTagcolUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTagcolUpdateLog get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTagcolUpdateLog doSave(DtTagcolUpdateLog m);

	/**
	 *
	 * @param content
	 * @param db
	 * @param ip
	 * @return
	 */
	DtTagcolUpdateLog loggingUpdate(String content,DtSetCol db, String ip);

	/**
	 *
	 * @param content
	 * @param body
	 * @param ip
	 * @return
	 */
	DtTagcolUpdateLog loggingNew(String content,DtSetCol body,String ip);

	/**
	 *
	 * @param content
	 * @param db
	 * @param ip
	 * @return
	 */
	DtTagcolUpdateLog loggingDelete(String content,DtSetCol db,String ip);

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
}
