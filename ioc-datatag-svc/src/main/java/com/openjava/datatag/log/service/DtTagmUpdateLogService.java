package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.log.query.DtTagmUpdateLogDBParam;

/**
 * 标签模型日志业务层接口
 * @author zmk
 *
 */
public interface DtTagmUpdateLogService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTagmUpdateLog> query(DtTagmUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTagmUpdateLog> queryDataOnly(DtTagmUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTagmUpdateLog get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTagmUpdateLog doSave(DtTagmUpdateLog m);

	/**
	 *
	 * @param modifyContent
	 * @param oldContent
	 * @param db
	 * @param ip
	 * @return
	 */
	DtTagmUpdateLog loggingUpdate(String modifyContent,String oldContent, DtTaggingModel db, String ip);

	/**
	 *
	 * @param modifyContent
	 * @param db
	 * @param ip
	 * @return
	 */
	DtTagmUpdateLog loggingNew(String modifyContent, DtTaggingModel db,String ip);

	/**
	 *
	 * @param db
	 * @param ip
	 * @return
	 */
	DtTagmUpdateLog loggingDelete(DtTaggingModel db,String ip);


}
